# Android BLE & 24/7 Connectivity SDK Guide (Staff Level)

Building a robust Bluetooth Low Energy (BLE) SDK in Android—especially one expected to run 24/7 (like a Passive Keyless Entry system for cars, a smartwatch sync engine, or a continuous glucose monitor)—is one of the most mechanically complex tasks in Android development. 

At the 8+ YOE level (critical for Harman/JBL/Automotive roles), you aren't just calling `connectGatt()`. You must navigate aggressive OEM battery savers (Samsung, Xiaomi), Android 12+ permission overhauls, the physical limitations of the Bluetooth radio, and **streaming high-frequency sensor data flawlessly**.

---

## 1. Scanning & Discovering Devices

Before you can connect, you must find the peripheral. Android's `BluetoothLeScanner` is notorious for draining battery if used incorrectly.

### Basic to Advanced Scanning Snippets

**❌ Basic / Junior Approach:**
Scanning for everything without filters. This destroys battery and gets severely throttled by the OS if left running.
```kotlin
val scanner = bluetoothAdapter.bluetoothLeScanner
// Bad: No filters, no settings, wakes up CPU for every single BLE device in the room.
scanner.startScan(object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        if (result.device.name == "HarmanCar") connect(result.device)
    }
})
```

**✅ Advanced / Staff Approach (Hardware Filtering):**
Pass filtering to the actual Bluetooth Hardware Chip (HCI). The main CPU stays asleep until a device matching the exact UUID is physically detected.

```kotlin
val scanner = bluetoothAdapter.bluetoothLeScanner

// 1. Hardware Filter (Only wake CPU if device advertises this exact Service UUID)
val filter = ScanFilter.Builder()
    .setServiceUuid(ParcelUuid(UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")))
    .build()

// 2. Power Settings
val settings = ScanSettings.Builder()
    // Use SCAN_MODE_LOW_LATENCY ONLY when UI is open.
    // Use SCAN_MODE_LOW_POWER for background scanning.
    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    // Only report when the device first appears or changes significantly
    .setReportDelay(0) 
    .build()

val scanCallback = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        val device = result.device
        val rssi = result.rssi // Signal strength (e.g., -60 dBm)
        Log.d("BLE", "Found Harman Device: ${device.address} at $rssi dBm")
        // Stop scanning the moment you find it to save battery!
        scanner.stopScan(this)
        connectToDevice(device)
    }
}

scanner.startScan(listOf(filter), settings, scanCallback)
```

---

## 2. The 24/7 SDK Architecture & Connecting

If the requirement is "The phone must connect to the car/headphone even if the user completely swipes the app out of the Recent Apps list (force kill)," you must use a strictly defined architectural pattern.

### The ONLY Way to Survive 24/7: The Foreground Service
You cannot rely on `WorkManager` (15-minute minimum intervals) or `AlarmManager` (inexact in Doze mode) for continuous BLE scanning and connection maintenance. 

You **must** run a Foreground Service of type `connectedDevice`.

```xml
<!-- AndroidManifest.xml -->
<service 
    android:name=".ble.BleConnectionService"
    android:foregroundServiceType="connectedDevice" 
    android:exported="false" />
```

### The Auto-Connect Trade-off (`autoConnect = true`)

When calling `device.connectGatt()`, the `autoConnect` boolean is the most misunderstood flag in Android BLE.

*   `autoConnect = false`: Direct connection attempt. Aggressive, fast, times out in ~30 seconds if the device isn't nearby.
*   `autoConnect = true`: Background connection attempt. Adds the device MAC address to the OS Bluetooth Controller's hardware allowlist. The OS handles scanning passively. It never times out.

| AutoConnect Trade-off |
| :--- |
| **🟢 PRO (`true`):** Zero battery drain. The OS Bluetooth hardware monitors for the MAC address without waking the main CPU. It automatically reconnects if you walk away and come back hours later.
| **🔴 CON (`true`):** Extremely slow. Can take up to 2-3 minutes to connect even if the device is right next to the phone.
| **🛠️ Staff Advice (The Hybrid Pattern):** Use `connectGatt(autoConnect = false)` when the app UI is open for instant gratification. When the user backgrounds the app, drop the connection, and immediately call `connectGatt(autoConnect = true)` to enter passive, infinite background monitoring.

### Advanced Connection & Callback Setup Snippet

```kotlin
var bluetoothGatt: BluetoothGatt? = null

fun connectToDevice(device: BluetoothDevice, isUiOpen: Boolean) {
    // Determine the hybrid auto-connect strategy
    val autoConnect = !isUiOpen 
    
    // TRANSPORT_LE forces Android to use Bluetooth Low Energy instead of Classic BT
    bluetoothGatt = device.connectGatt(context, autoConnect, gattCallback, BluetoothDevice.TRANSPORT_LE)
}

private val gattCallback = object : BluetoothGattCallback() {
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BLE", "Connected! Now discovering services...")
                // MUST discover services before you can read/write data
                gatt.discoverServices() 
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BLE", "Disconnected safely.")
                gatt.close() // ALWAYS close to prevent memory/hardware leaks
            }
        } else {
            // Error occurred (e.g., GATT_ERROR 133). Clean up and retry.
            Log.e("BLE", "GATT Error: $status")
            gatt.close()
            // Implement Exponential Backoff Retry here
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d("BLE", "Services ready! Can now execute read/write queue.")
            // Trigger the command queue to pull initial state
        }
    }
}
```

---

## 3. Best Practices for Developing a BLE SDK

When building an SDK that other developers will consume, encapsulate the nightmare of Android BLE inside a clean API (using Kotlin Coroutines/Flows).

### A. Isolate the Bluetooth Thread & The Command Queue (GATT 133 Errors)
**The Problem:** The most notorious error in Android BLE is `GATT_ERROR 133`. It is a catch-all hardware failure. A primary cause is firing multiple BLE operations (Read, Write, MTU Request) simultaneously from different threads. The hardware queue gets overwhelmed and crashes.
**The Fix:** You must create an internal **Command Queue** (using a `Mutex` or single-threaded Dispatcher) to serialize all BLE commands. Wait for the exact callback before sending the next command.

### Advanced Coroutine Queueing Snippet

```kotlin
class BleCommandQueue {
    // 1. Single Threaded Dispatcher ensures strict FIFO execution
    private val bleDispatcher = Dispatchers.IO.limitedParallelism(1)
    
    // 2. Holds the current Continuation so the GattCallback can resume it
    private var currentContinuation: CancellableContinuation<Boolean>? = null

    suspend fun writeCharacteristic(
        gatt: BluetoothGatt, 
        characteristic: BluetoothGattCharacteristic, 
        payload: ByteArray
    ): Boolean = withContext(bleDispatcher) {
        
        // 3. Suspend the coroutine until the hardware finishes the write
        suspendCancellableCoroutine { continuation ->
            currentContinuation = continuation
            
            characteristic.value = payload
            val success = gatt.writeCharacteristic(characteristic)
            
            if (!success) {
                // Instantly resume with failure if OS rejects the call
                continuation.resume(false)
                currentContinuation = null
            }
        }
    }

    // 4. Called from the BluetoothGattCallback when hardware confirms success
    fun onWriteCompleted(status: Int) {
        val success = (status == BluetoothGatt.GATT_SUCCESS)
        currentContinuation?.resume(success)
        currentContinuation = null
    }
}
```
*How it works:* If 5 different ViewModels attempt to write to the Car simultaneously, `bleDispatcher` forces them into a single-file line. Coroutine #1 suspends and waits. When `BluetoothGattCallback` fires, it calls `onWriteCompleted()`, resuming Coroutine #1. *Only then* does Coroutine #2 begin. This eliminates 90% of `133` crashes.

---

## 4. Advanced Flow Architecture for BLE SDK (PPG, IMU, PAC, BMM)

When building a high-end health or wearables SDK (e.g., streaming physical sensors to the phone), you are dealing with high-frequency byte arrays arriving asynchronously via `onCharacteristicChanged`.

You must translate raw byte arrays into Kotlin `Flow<T>` streams so the consumer of the SDK can collect them cleanly without worrying about Bluetooth callbacks.

### A. The SDK Interfaces (Consumer Facing)
A good SDK hides the `BluetoothGatt` entirely. The consumer app only sees business-level interfaces.

```kotlin
// Consumer sees this clean interface:
interface HarmanWearableSdk {
    val connectionState: StateFlow<ConnectionState>
    
    // PAC (Protocol App Command) - Bidirectional control channel
    suspend fun sendCommand(command: PacCommand): Result<Unit>
    
    // Sensor Streams
    fun observePpgSensor(): Flow<PpgData> // Photoplethysmography (Heart Rate/Blood volume)
    fun observeImuSensor(): Flow<ImuData> // Inertial Measurement Unit (Accel/Gyroscope)
    fun observeBmmSensor(): Flow<BmmData> // Bio-Magnetic Measurement / Battery Mgmt
}

data class PpgData(val heartRate: Int, val spo2: Int)
data class ImuData(val accelX: Float, val accelY: Float, val accelZ: Float)
```

### B. Streaming High-Frequency Data with MutableSharedFlow
Sensors like **IMU (Inertial Measurement Unit)** or **PPG** stream at 50Hz–100Hz. You cannot use `StateFlow` for this, because `StateFlow` conflates (drops) intermediate values if the collector is too slow. You must use `MutableSharedFlow` with a buffer.

```kotlin
class BleSensorManager : HarmanWearableSdk {

    // 1. Define internal hot streams with extra buffer capacity to avoid dropping frames
    private val _imuFlow = MutableSharedFlow<ImuData>(
        extraBufferCapacity = 64, 
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override fun observeImuSensor(): Flow<ImuData> = _imuFlow.asSharedFlow()

    private val _ppgFlow = MutableSharedFlow<PpgData>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override fun observePpgSensor(): Flow<PpgData> = _ppgFlow.asSharedFlow()

    // 2. The raw callback from the OS (runs on a weird Binder thread)
    fun onCharacteristicChanged(characteristic: BluetoothGattCharacteristic) {
        val payload: ByteArray = characteristic.value

        when (characteristic.uuid) {
            IMU_UUID -> {
                // Parse IMU bytes (e.g., 6 bytes for X,Y,Z axes)
                val imuData = ImuParser.parse(payload)
                // Use tryEmit because we cannot 'suspend' inside an OS callback
                _imuFlow.tryEmit(imuData)
            }
            PPG_UUID -> {
                val ppgData = PpgParser.parse(payload)
                _ppgFlow.tryEmit(ppgData)
            }
            PAC_UUID -> {
                // Handle Protocol App Command responses
                handlePacResponse(payload)
            }
        }
    }
}
```

### C. Protocol App Commands (PAC) - Request/Response via Flows
If the app sends a PAC command (e.g., "Set Alarms" or "Calibrate IMU"), the response arrives completely asynchronously hours or milliseconds later in `onCharacteristicChanged`. We bridge this using `suspendCancellableCoroutine` or a `SharedFlow` filter.

```kotlin
// Example: Sending a complex PAC command and waiting for an ACK from the device
override suspend fun sendCommand(command: PacCommand): Result<Unit> {
    return try {
        val payloadBytes = command.toByteArray()
        
        // 1. Add write to the command queue (eliminates GATT 133)
        commandQueue.writeCharacteristic(gatt, pacCharacteristic, payloadBytes)
        
        // 2. We use a SharedFlow of incoming PAC responses to wait for our specific ACK
        val isAckReceived = withTimeout(5000L) {
            pacResponseFlow.first { responseBytes ->
                // Look for the specific command ID matching our request
                responseBytes[0] == command.getId() && responseBytes[1] == ACK_BYTE
            }
        }
        
        Result.success(Unit)
    } catch (e: TimeoutCancellationException) {
        Result.failure(Exception("Device did not ACK the PAC command in time"))
    }
}
```

---

## 5. Surviving OEM Battery Savers (The Real World)

This is what separates juniors from staff engineers. An SDK works perfectly on a Pixel, but dies after 30 minutes on a Samsung or Xiaomi. Why?

### A. The "Swipe to Kill" Problem
When a user swipes an app from the "Recents" menu:
*   **Stock Android / Pixel:** Kills the app, but restarts the `Service` returning `START_STICKY` a few seconds later.
*   **Xiaomi / Huawei / OnePlus:** Force-Stops the app entirely. Unregisters all BroadcastReceivers. Kills the Foreground Service. The app is dead until the user manually taps the launcher icon.

### 🚗 Use Case: Phone-As-A-Key (PAAK) vs Wearable Sync
If you are building an SDK that unlocks a car door or pulls IMU data without the user opening the app, it **must** resurrect itself.

| Resurrection Strategy Trade-offs |
| :--- |
| **1. The Companion Device Manager (API 26+)** |
| Allows you to register the car/wearable's MAC address with the OS. If the OS sees the device's BLE advertisement, it wakes your app up! |
| **🟢 PRO:** Blessed by Google. Works reliably for 24/7 IMU/PPG wearables. |
| **🔴 CON:** Requires a system UI prompt asking the user to pair their device. |
| **2. BLE PendingIntent Filtering (API 26+)** |
| Register a `PendingIntent` with the OS Bluetooth scanner. The OS wakes your app when the specific Service UUID is seen. |
| **🟢 PRO:** Completely silent background wake-up. No UI prompts. |
| **🔴 CON:** Aggressive Chinese OEMs still ignore the PendingIntent if the app is heavily restricted. |

---

## 6. Staff-Level Interview Summary 📝

If asked: **"How would you design an SDK that connects to a Harman wearable streaming high-frequency sensors (IMU/PPG) 24/7?"**

1.  **Architecture:** "I would run a `ForegroundService` tied to a persistent Notification. This prevents standard Android Doze from killing our BLE state machine."
2.  **SDK Interfaces & Flows:** "I would completely hide `BluetoothGatt` from the consumer app. I'd expose a clean interface returning `SharedFlow<ImuData>` and `SharedFlow<PpgData>`. I use `SharedFlow` with a drop-oldest buffer because `StateFlow` would conflate and drop intermediate 100Hz sensor frames."
3.  **Command Serialization (PAC):** "BLE hardware queues crash with `GATT_ERROR 133` if overwhelmed by Protocol App Commands (PAC). I architect the SDK using a single-threaded Coroutine Dispatcher (`limitedParallelism`), converting async GattCallbacks into suspending Coroutine functions to ensure strictly sequential Read/Write commands. I handle PAC request/responses using `withTimeout` and `Flow.first()`."
4.  **OEM Resilience:** "Because Xiaomi and Samsung brutally kill background tasks, I would integrate the **Companion Device Manager** or **PendingIntent BLE Scanning**. This allows the Android OS to natively wake our SDK up to pull PPG/IMU logs when the wearable is detected."
