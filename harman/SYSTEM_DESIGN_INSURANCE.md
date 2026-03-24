# System Design: Accidental Insurance Claim Flow (Staff Level)

This document outlines the architecture, data flow, and critical edge cases for an enterprise insurance application focused on complex media rendering, offsite offline capabilities, and anti-fraud authentication.

---

## 1. Requirements & Constraints
*   **Functional:** Guide users through an accident reporting wizard. Capture 30+ high-res photos. 
*   **Non-Functional (Performance):** Uploading 500MB+ of media must not OutOfMemory (OOM) the app. Must succeed over spotty highway 3G connections.
*   **Non-Functional (Security):** Cryptographic proof that photos were taken precisely at the accident scene and haven't been tampered with.

---

## 2. High-Level Architecture Diagram
```mermaid
graph TD;
    CameraX[CameraX Surface] --> PhotoFile[Internal Storage WebP];
    PhotoFile --> DraftRepo[Room Database (Drafts)];
    DraftRepo --> WMChain[WorkManager Media Chain];
    WMChain[Worker 1: Compress] --> WMChain2[Worker 2: AWS S3 Upload];
    WMChain2 --> WMChain3[Worker 3: API Finalize];
```

---

## 3. Core Components & Media Management

### A. Preventing Out-Of-Memory (OOM)
Putting 30 bitmaps taken from a modern 48MP smartphone sensor into active RAM simultaneously will crash the JVM instantly.
-   **Implementation:** Never load raw Bitmaps into memory buffers.
-   When `ImageCapture` returns a byte array, stream it directly to a local `File` on the disk.
-   Use `inSampleSize` within `BitmapFactory.Options` to aggressively downscale the dimensions. Recompress the downscaled bitmap locally into `.WebP` to drastically slash the disk footprint.

### B. The Resilient Upload Pipeline
The user is standing on a highway with 1 bar of cellular service. If you trigger a Retrofit loop for 30 files, it will inevitably time out and fail.
-   **Implementation:** Delegate the upload entirely to `WorkManager`.
-   Enforce constraints: `setRequiredNetworkType(NetworkType.CONNECTED)`.
-   Use **Chained Workers** for atomicity:
    ```kotlin
    val compressWorker = OneTimeWorkRequest<CompressWorker>()
    val uploadWorkers = photos.map { OneTimeWorkRequest<UploadWorker>(input = it.uri) }
    val finalizeWorker = OneTimeWorkRequest<SubmitClaimWorker>()

    WorkManager.getInstance(ctx)
        .beginWith(compressWorker)
        .then(uploadWorkers) // These run in parallel
        .then(finalizeWorker) // Runs ONLY when all uploads succeed
        .enqueue()
    ```
-   If the 3G drops, WorkManager automatically pauses and resumes days later without losing state.

---

## 4. State & Resilience

**The Scenario:** The insurance inspector is recording a sweeping 360-degree video of the damaged vehicle to attach to the claim. To get a better view of the undercarriage, they rotate their phone from Portrait to Landscape.
-   **The Trap:** A screen rotation destroys and recreates the Android Activity. The camera session terminates, and the video recording instantly stops midway.
-   **The Solution:** 
    1.  Bypass the default lifecycle destruction by declaring `android:configChanges="orientation|screenSize"` in the `AndroidManifest.xml` for this specific Camera Activity.
    2.  The Activity stays alive, maintaining the active Camera2 session.
    3.  You manually hook into `onConfigurationChanged()` to smoothly animate the UI buttons (flash, shutter) by 90-degrees without ever dropping the live video feed.

---

## 5. Security & Anti-Fraud (Authenticity)

Insurance fraud via Photoshop is a massive liability. How do you prove the photo is real?

### A. Hardware-Backed Image Attestation
It's not enough to read EXIF GPS boundaries, as users can artificially spoof mock locations or alter EXIF metadata via 3rd party apps.
1.  **Key Generation:** On app launch, generate an ECDSA key pair bounded to the Android Keystore Titan M chip.
2.  **Hashing:** The exact millisecond the user clicks the CameraX "Snap" button and you hold the raw byte array in memory, generate a SHA-256 hash of those exact bytes.
3.  **Cryptographic Signing:** Sign the SHA-256 hash with the hardware-backed Key. 
4.  **Verification:** Attach the signature payload and the `KeyAttestation` Certificate Chain (provided by the OS, proving the key is physically inside non-rooted hardware) directly to the API upload. 
5.  If the user intercepts the file via proxy and alters *one pixel*, the Backend will see the SHA-256 hash doesn't match the un-spoofable hardware signature.
