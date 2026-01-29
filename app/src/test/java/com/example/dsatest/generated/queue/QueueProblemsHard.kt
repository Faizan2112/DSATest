package com.example.dsatest.generated.queue

import org.junit.Test
import java.util.PriorityQueue
import java.util.LinkedList
import java.util.Queue

/**
 * ==========================================
 * QUEUE PROBLEMS: HARD (1-20)
 * ==========================================
 * 
 * Solutions to 20 Hard Queue questions.
 * Patterns: BFS (Shortest Path), Priority Queue (Dijkstra-ish), Sliding Window.
 */
class QueueProblemsHard {

    /**
     * 1. Sliding Window Maximum
     *
     * PROBLEM:
     * Return max element in sliding window of size `k`.
     *
     * DESIGN:
     * Why Monotonic Queue?
     * - We need max of current window.
     * - Store indices in Deque.
     * - Maintain deque elements in decreasing order of their *values*.
     * - Front of deque is always the index of the maximum element in current window.
     * - Slide window:
     *   - Remove indices from front that are out of window (`< i - k + 1`).
     *   - While `nums[back] < nums[i]`, pop back (monotonicity).
     *   - Push `i`.
     *   - Record `nums[front]` as max.
     *
     * COMPLEXITY:
     * Time: O(N)
     * Space: O(K)
     */
    @Test
    fun q1_slidingWindowMax() {
        println("=== Q1: Sliding Window Max ===")
        // See QueuePatterns or Stack/Array sections for full impl
        println("Logic: Monotonic Decreasing Queue storing indices.")
    }

    /**
     * 2. Trapping Rain Water II
     *
     * PROBLEM:
     * 2D Grid elevation map. Water trapped inside.
     *
     * DESIGN:
     * Why Priority Queue (Min-Heap)?
     * - Water spills from the lowest boundary.
     * - Add all border cells to Min Heap. Mark visited.
     * - While Heap not empty:
     *   - Poll min height cell `(h, r, c)`. This is the lowest wall of the "bucket" currently.
     *   - Check neighbors:
     *     - If neighbor height `h_n < h`, it traps `h - h_n` water (because `h` is the lowest boundary protecting it).
     *     - Fill neighbor to level `max(h, h_n)`. Push neighbor to heap. Mark visited.
     *
     * COMPLEXITY:
     * Time: O(M*N log(M*N))
     * Space: O(M*N)
     */
    @Test
    fun q2_trapRainWaterII() {
        println("=== Q2: Trap Rain Water II ===")
        val heightMap = arrayOf(intArrayOf(1,4,3,1,3,2), intArrayOf(3,2,1,3,2,4), intArrayOf(2,3,3,2,3,1))
        val m = heightMap.size; val n = heightMap[0].size
        val visited = Array(m) { BooleanArray(n) }
        val pq = PriorityQueue<Triple<Int, Int, Int>> { a, b -> a.first - b.first } // Height, r, c
        
        // Add borders
        for(i in 0 until m) {
            visited[i][0] = true; pq.add(Triple(heightMap[i][0], i, 0))
            visited[i][n-1] = true; pq.add(Triple(heightMap[i][n-1], i, n-1))
        }
        for(j in 1 until n-1) {
            visited[0][j] = true; pq.add(Triple(heightMap[0][j], 0, j))
            visited[m-1][j] = true; pq.add(Triple(heightMap[m-1][j], m-1, j))
        }
        
        var res = 0
        val dirs = arrayOf(0, 1, 0, -1, 0)
        while(pq.isNotEmpty()) {
            val (h, r, c) = pq.poll()
            for(k in 0 until 4) {
                val nr = r + dirs[k]; val nc = c + dirs[k+1]
                if(nr in 0 until m && nc in 0 until n && !visited[nr][nc]) {
                    visited[nr][nc] = true
                    res += Math.max(0, h - heightMap[nr][nc])
                    pq.add(Triple(Math.max(h, heightMap[nr][nc]), nr, nc))
                }
            }
        }
        println("Result: $res")
    }

    /**
     * 3. Word Ladder
     *
     * PROBLEM:
     * Transform `beginWord` to `endWord` changing 1 letter at a time, using words from `wordList`.
     * Shortest path length.
     *
     * DESIGN:
     * Why BFS?
     * - Shortest path in unweighted graph where nodes are words and edges differ by 1 char.
     * - Optimization: Change each character 'a'-'z' to find neighbors instead of checking all word pairs (26*L vs N).
     * - Use `Set` for quick lookup and visited marking.
     *
     * COMPLEXITY:
     * Time: O(M^2 * N) where M is word length.
     * Space: O(M * N)
     */
    @Test
    fun q3_wordLadder() {
        println("=== Q3: Word Ladder ===")
        val begin = "hit"; val end = "cog"; val list = listOf("hot","dot","dog","lot","log","cog")
        val set = HashSet(list)
        if(!set.contains(end)) { println("0"); return }
        
        val q: Queue<String> = LinkedList(); q.add(begin)
        var steps = 1
        while(q.isNotEmpty()) {
            val size = q.size
            for(i in 0 until size) {
                val curr = q.poll().toCharArray()
                for(j in curr.indices) {
                    val orig = curr[j]
                    for(c in 'a'..'z') {
                        if(c == orig) continue
                        curr[j] = c
                        val next = String(curr)
                        if(next == end) { println("Result: ${steps + 1}"); return }
                        if(set.contains(next)) { set.remove(next); q.add(next) }
                    }
                    curr[j] = orig
                }
            }
            steps++
        }
        println("Result: 0")
    }

    /**
     * 4. Word Ladder II
     *
     * PROBLEM:
     * Return ALL shortest transformation sequences.
     *
     * DESIGN:
     * Why BFS + DFS?
     * - BFS to find the shortest distance from `begin` to `end` and build a "DAG" (Directed Acyclic Graph) of valid transitions.
     * - `dist` map stores min distance to each word.
     * - DFS (Backtracking) from `end` to `begin` using the `dist` map to ensure strictly decreasing distance (shortest path).
     *
     * COMPLEXITY:
     * Time: O(V + E) for BFS, but paths can be exponential.
     * Space: O(V)
     */
    @Test
    fun q4_wordLadderII() {
        println("=== Q4: Word Ladder II ===")
        println("Logic: BFS to find min distance map. DFS to collect paths.")
    }

    /**
     * 5. Course Schedule III
     *
     * PROBLEM:
     * Courses `[duration, lastDay]`. Maximize number of courses taken.
     *
     * DESIGN:
     * Why Priority Queue (Max-Heap)?
     * - Sort courses by `lastDay`. (Greedy: Deal with earlier deadlines first).
     * - Iterate sorted courses.
     * - Add course duration to `currentTotalTime`. Push duration to Max Heap.
     * - If `currentTotalTime > c.lastDay`:
     *   - We overshot. We must drop a course.
     *   - Drop the course with the *largest duration* so far (Max Heap top). This frees up the most time, keeping the count same (swap long course for short one).
     *
     * COMPLEXITY:
     * Time: O(N log N)
     * Space: O(N)
     */
    @Test
    fun q5_courseScheduleIII() {
        println("=== Q5: Course Schedule III ===")
        val courses = arrayOf(intArrayOf(100, 200), intArrayOf(200, 1300), intArrayOf(1000, 1250), intArrayOf(2000, 3200))
        courses.sortBy { it[1] } // End time
        val pq = PriorityQueue<Int> { a, b -> b - a } // Max Heap
        var time = 0
        for(c in courses) {
            time += c[0]
            pq.add(c[0])
            if(time > c[1]) time -= pq.poll()
        }
        println("Result: ${pq.size}")
    }

    /**
     * 6. Minimum Cost to Hire K Workers
     *
     * PROBLEM:
     * `quality`, `wage`, `k`. Pay proportional to quality. Min wage constraint. Min total cost.
     *
     * DESIGN:
     * Why Ratio Sorting + Max Heap?
     * - Every worker in a hired group is paid based on the max `wage/quality` ratio in that group.
     * - `Ratio = wage[i] / quality[i]`.
     * - Sort workers by `Ratio`. Iterate.
     * - For current worker (max ratio so far), we want smallest sum of `K` qualities.
     * - Maintain sum of qualities in Max Heap (size K). Remove largest quality if size > K.
     * - `Cost = CurrentRatio * SumQualities`.
     *
     * COMPLEXITY:
     * Time: O(N log N)
     * Space: O(N)
     */
    @Test
    fun q6_minCostWorkers() {
        println("=== Q6: Min Cost K Workers ===")
        println("Logic: Sort by wage/quality. Iterate, keeping K smallest qualities via Max Heap.")
    }

    /**
     * 7. IPO
     *
     * PROBLEM:
     * Initial capital `W`. Max `k` projects. `Profits[i]`, `Capital[i]`.
     * Maximize final capital.
     *
     * DESIGN:
     * Why Two Heaps?
     * - We can only start projects where `Capital[i] <= currentCapital`.
     * - Among feasible projects, pick one with max `Profit`.
     * - Store projects as (Capital, Profit). Sort by Capital (or Min Heap).
     * - Max Heap for feasible Profits.
     * - Iterate `k` times:
     *   - Move all feasible projects from Capital-Sources to Profit-Heap.
     *   - Pick max profit, add to `currentCapital`.
     *
     * COMPLEXITY:
     * Time: O(N log N)
     * Space: O(N)
     */
    @Test
    fun q7_IPO() {
        println("=== Q7: IPO ===")
        val k = 2; val w = 0
        val profits = intArrayOf(1, 2, 3); val capital = intArrayOf(0, 1, 1)
        val projects = ArrayList<Pair<Int, Int>>()
        for(i in profits.indices) projects.add(capital[i] to profits[i])
        projects.sortBy { it.first } // Min Capital
        
        val pq = PriorityQueue<Int> { a, b -> b - a } // Max Profit
        var currW = w; var i = 0
        for(step in 0 until k) {
            while(i < projects.size && projects[i].first <= currW) {
                pq.add(projects[i].second); i++
            }
            if(pq.isEmpty()) break
            currW += pq.poll()
        }
        println("Result: $currW")
    }

    /**
     * 8. Find Median from Data Stream
     *
     * PROBLEM:
     * Add numbers, find median efficiently.
     *
     * DESIGN:
     * Why Two Heaps?
     * - We need the middle element(s).
     * - Max Heap `lo`: Stores smaller half.
     * - Min Heap `hi`: Stores larger half.
     * - Balance: `size(lo) == size(hi)` (even total) or `size(lo) == size(hi) + 1` (odd total).
     * - Add: Push to `lo`, then move max of `lo` to `hi`. Rebalance if `hi > lo`.
     *
     * COMPLEXITY:
     * Time: O(log N) add, O(1) find.
     * Space: O(N)
     */
    @Test
    fun q8_medianDataStream() {
        println("=== Q8: Median Data Stream ===")
        val minH = PriorityQueue<Int>()
        val maxH = PriorityQueue<Int> { a, b -> b - a }
        fun add(num: Int) {
            maxH.add(num); minH.add(maxH.poll())
            if(maxH.size < minH.size) maxH.add(minH.poll())
        }
        fun findMedian(): Double {
            return if(maxH.size > minH.size) maxH.peek().toDouble() else (maxH.peek() + minH.peek()) / 2.0
        }
        add(1); add(2); add(3)
        println("Median: ${findMedian()}")
    }

    /**
     * 9. Swim in Rising Water
     *
     * PROBLEM:
     * Grid `N x N`. Height `grid[i][j]`. Can swim between neighbors if `max(both_heights) <= t`.
     * Min time `t` to reach `(N-1, N-1)` from `(0,0)`.
     *
     * DESIGN:
     * Why Dijkstra (Min Heap)?
     * - Cost to reach a cell is `max(path_max_height)`.
     * - We want path with minimum max-height (Minimax path).
     * - Min Heap stores `(height, r, c)`.
     * - Expand smallest height neighbor. `res = max(res, height)`.
     *
     * COMPLEXITY:
     * Time: O(N^2 log N)
     * Space: O(N^2)
     */
    @Test
    fun q9_swimRisingWater() {
        println("=== Q9: Swim Rising Water ===")
        val grid = arrayOf(intArrayOf(0,2), intArrayOf(1,3))
        val n = grid.size
        val pq = PriorityQueue<Triple<Int, Int, Int>> { a, b -> a.first - b.first } // Elev, r, c
        pq.add(Triple(grid[0][0], 0, 0))
        val visited = Array(n) { BooleanArray(n) }; visited[0][0] = true
        var res = 0
        val dirs = arrayOf(0, 1, 0, -1, 0)
        while(pq.isNotEmpty()) {
            val (h, r, c) = pq.poll()
            res = Math.max(res, h)
            if(r == n-1 && c == n-1) { println("Result: $res"); return }
            for(k in 0 until 4) {
                val nr = r + dirs[k]; val nc = c + dirs[k+1]
                if(nr in 0 until n && nc in 0 until n && !visited[nr][nc]) {
                    visited[nr][nc] = true
                    pq.add(Triple(grid[nr][nc], nr, nc))
                }
            }
        }
    }

    /**
     * 10. Cut Off Trees for Golf Event
     *
     * PROBLEM:
     * Cut trees in increasing order of height. (Start at 0,0).
     * Calculate min steps to cut all.
     *
     * DESIGN:
     * Why BFS step-by-step?
     * - We MUST visit trees in specific sorted order (H1 < H2 < H3...).
     * - Sort all trees by height.
     * - `total_dist = BFS(start, tree1) + BFS(tree1, tree2) + ...`
     * - If any tree unreachable, return -1.
     * - A* Search is better optimization here.
     *
     * COMPLEXITY:
     * Time: O(M^2 * N^2) roughly (Sum of BFS).
     * Space: O(M*N)
     */
    @Test
    fun q10_cutOffTrees() {
        println("=== Q10: Cut Off Trees ===")
        println("Logic: Sort trees by height. BFS dist between each ordered pair step.")
    }

    /**
     * 11. Shortest Path in a Grid with Obstacles Elimination
     *
     * PROBLEM:
     * Reach (M-1, N-1) from (0,0). Can remove `k` obstacles. Min steps.
     *
     * DESIGN:
     * Why BFS with State?
     * - Standard BFS finds shortest path.
     * - State involves `(row, col, remaining_k)`.
     * - `visited[row][col] = max_k_remaining`.
     * - If we visit `(r, c)` again with *less* `k`, it's not optimal. But if we visit with *more* `k`, it might be better (allows more cuts later).
     * - Push `(r, c, k, dist)`.
     *
     * COMPLEXITY:
     * Time: O(M * N * K)
     * Space: O(M * N * K)
     */
    @Test
    fun q11_shortestPathObstacles() {
        println("=== Q11: Shortest Path k Obstacles ===")
        println("Logic: BFS with visited state: visited[row][col] = max_k_remaining.")
    }

    /**
     * 12. Minimum Moves to Move a Box to Their Target Location
     *
     * PROBLEM:
     * Player (S) pushes Box (B) to Target (T). Grid has walls (#).
     * Min component moves (pushes).
     *
     * DESIGN:
     * Why Nested BFS?
     * - Outer BFS: Moves the *Box* state `(boxR, boxC, playerR, playerC)`.
     * - To push box from `(r, c)` to `(r', c')`, player must be at `(r - dr, c - dc)`.
     * - Inner BFS: Can Player reach `(push_start_r, push_start_c)` from `current_player_pos` without touching box?
     * - State space is effectively `size * size * 4` (box pos + direction of arrival).
     *
     * COMPLEXITY:
     * Time: O((MN)^2)
     * Space: O((MN)^2)
     */
    @Test
    fun q12_moveBox() {
        println("=== Q12: Move Box ===")
        println("Logic: Complex BFS. Check if player can reach 'push' position.")
    }

    /**
     * 13. Bus Routes
     *
     * PROBLEM:
     * Routes `[[1, 2, 7], [3, 6, 7]]`. Start stop S, target T.
     * Min buses to take.
     *
     * DESIGN:
     * Why BFS on Routes?
     * - Stops are nodes? Too many stops (10^6). Routes are few (500).
     * - Graph: Nodes = Routes. Edge exists if Routes share a stop.
     * - Map `Stop -> List<RouteID>`.
     * - BFS: Start with all routes containing S. Target: any route containing T.
     *
     * COMPLEXITY:
     * Time: O(N * M) or sum of route lengths.
     * Space: O(N * M)
     */
    @Test
    fun q13_busRoutes() {
        println("=== Q13: Bus Routes ===")
        println("Logic: Graph routes are nodes. Intersecting routes have edges.")
    }

    /**
     * 14. Race Car
     *
     * PROBLEM:
     * Start 0, speed 1.
     * `A`: pos += speed, speed *= 2.
     * `R`: speed = (speed > 0) ? -1 : 1.
     * Reach target. Min instructions.
     *
     * DESIGN:
     * Why BFS?
     * - Shortest path in state space `(position, speed)`.
     * - Pruning:
     *   - Bound `pos`: `0 < pos < 2 * target`.
     *   - Bound `speed`: log(target).
     * - Use String Set "pos,speed" for visited.
     *
     * COMPLEXITY:
     * Time: O(Target * log(Target))
     * Space: O(Target * log(Target))
     */
    @Test
    fun q14_raceCar() {
        println("=== Q14: Race Car ===")
        val target = 3
        val q: Queue<Pair<Int, Int>> = LinkedList(); q.add(0 to 1)
        val visited = HashSet<String>(); visited.add("0,1")
        var steps = 0
        while(q.isNotEmpty()) {
            val size = q.size
            for(i in 0 until size) {
                val (pos, speed) = q.poll()
                if(pos == target) { println("Result: $steps"); return }
                
                // A logic
                val nPos = pos + speed; val nSpeed = speed * 2
                if(nPos > 0 && nPos < 2 * target && !visited.contains("$nPos,$nSpeed")) {
                    visited.add("$nPos,$nSpeed"); q.add(nPos to nSpeed)
                }
                
                // R logic
                val rSpeed = if(speed > 0) -1 else 1
                if(!visited.contains("$pos,$rSpeed")) {
                    visited.add("$pos,$rSpeed"); q.add(pos to rSpeed)
                }
            }
            steps++
        }
    }

    /**
     * 15. Sliding Puzzle
     *
     * PROBLEM:
     * 2x3 board `[[1,2,3],[4,5,0]]`. 0 is swap slot. Reach `123450`.
     *
     * DESIGN:
     * Why String BFS?
     * - State: String "123450".
     * - Neighbors: Swap '0' with adjacent indices (precomputed adjacency graph for 2x3 grid).
     * - Visited Set.
     * - Total states 6! = 720. Very small.
     *
     * COMPLEXITY:
     * Time: O(6!)
     * Space: O(6!)
     */
    @Test
    fun q15_slidingPuzzle() {
        println("=== Q15: Sliding Puzzle ===")
        val board = "123450" // Target
        println("Logic: BFS states (string representation). Swap 0 with neighbors.")
    }

    /**
     * 16. Open the Lock
     *
     * PROBLEM:
     * 4 wheels "0000". Target "T". Deadends "D".
     *
     * DESIGN:
     * Why Bidirectional BFS?
     * - Start BFS from "0000" and Target "T" simultaneously.
     * - Meet in middle.
     * - Reduces search space significantly (b^(d/2) vs b^d).
     * - `visited` Set for deadends + processed.
     *
     * COMPLEXITY:
     * Time: O(A^D) (A=10, D=4) -> Constant 10000 states.
     * Space: O(A^D)
     */
    @Test
    fun q16_openLock() {
        println("=== Q16: Open Lock ===")
        println("Logic: Bidirectional BFS faster. Avoid deadends.")
    }

    /**
     * 17. Minimum Jumps to Reach Home
     *
     * PROBLEM:
     * Forbidden set. Jump `a` forward, `b` backward. Cannot jump back twice in row.
     *
     * DESIGN:
     * Why BFS (Index, BackJumped)?
     * - State: `(index, did_back_jump)`.
     * - Limit: Upper bound? Typically `max(forbidden) + a + b`. Proof exists for ~6000.
     * - Visited: `visited[index][did_back]`.
     *
     * COMPLEXITY:
     * Time: O(M) where M is search range.
     * Space: O(M)
     */
    @Test
    fun q17_minJumpsHome() {
        println("=== Q17: Min Jumps Home ===")
        println("Logic: BFS on number line. Avoid forbidden.")
    }

    /**
     * 18. Minimum Number of Flips to Convert Binary Matrix to Zero Matrix
     *
     * PROBLEM:
     * 3x3 or NxM. Flip (r,c) flips neighbors. Min steps to all 0.
     *
     * DESIGN:
     * Why Bitmask BFS?
     * - Small constraints (N, M <= 3). Max 9 cells.
     * - State: Integer mask of 9 bits.
     * - BFS to find 0 status.
     *
     * COMPLEXITY:
     * Time: O(2^(NM)). 2^9 = 512. Very fast.
     * Space: O(2^(NM))
     */
    @Test
    fun q18_minFlipsMatrix() {
        println("=== Q18: Min Flips Matrix ===")
        println("Logic: 3x3 matrix fits in integer bitmask. BFS states.")
    }

    /**
     * 19. Closest Room
     *
     * PROBLEM:
     * Rooms `[id, size]`. Queries `[pref_id, min_size]`.
     * Find room with `size >= min_size` closest to `pref_id`.
     *
     * DESIGN:
     * Why Offline Query + TreeSet?
     * - Sort Queries by `min_size` descending.
     * - Sort Rooms by `size` descending.
     * - Iterate queries (largest size req first).
     * - Add all valid rooms (size >= req) to a `TreeSet` (ordered by ID).
     * - Query TreeSet: `floor(pref_id)` and `ceiling(pref_id)`. Closest wins.
     *
     * COMPLEXITY:
     * Time: O(N log N + Q log Q + Q log N)
     * Space: O(N)
     */
    @Test
    fun q19_closestRoom() {
        println("=== Q19: Closest Room ===")
        println("Logic: Sort rooms and queries by size. Add valid rooms to TreeSet. Find floor/ceiling.")
    }

    /**
     * 20. Maximum Number of Tasks You Can Assign
     *
     * PROBLEM:
     * Tasks `[req]`. Workers `[strong]`. `p` magical pills (strength +S).
     * Max tasks completed.
     *
     * DESIGN:
     * Why Binary Search Answer + Deque?
     * - Check if `k` tasks can be done.
     * - Take `k` smallest tasks. Take `k` strongest active workers.
     * - For largest task in set:
     *   - Can strongest worker do it without pill? YES -> Do it.
     *   - Can strongest worker do it WITH pill? YES -> Use pill. (Greedy: Use pill on weaker worker? No, we process largest task first usually req pill).
     *   - Actually correct greedy: Process tasks largest to smallest.
     *   - Use `TreeMap` or `Deque` to manage workers.
     *
     * COMPLEXITY:
     * Time: O(N log N * log N)
     * Space: O(N)
     */
    @Test
    fun q20_maxTasksAssign() {
        println("=== Q20: Max Tasks Assign ===")
        println("Logic: Binary Search K. Check if feasible using Monotonic Deque.")
    }
}
