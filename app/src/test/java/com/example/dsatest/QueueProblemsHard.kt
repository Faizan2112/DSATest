package com.example.dsatest

import org.junit.Test
import java.util.PriorityQueue
import java.util.LinkedList
import java.util.Queue
import java.util.ArrayDeque

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
     * Logic: Monotonic Deque.
     */
    @Test
    fun q1_slidingWindowMax() {
        println("=== Q1: Sliding Window Max ===")
        // See QueuePatterns or Stack/Array sections
        println("Logic: Monotonic Queue.")
    }

    /**
     * 2. Trapping Rain Water II (3D)
     * Logic: Min Priority Queue on boundaries. Shrink inward.
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
     * Logic: BFS.
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
     * Logic: BFS (build graph) + DFS (paths).
     */
    @Test
    fun q4_wordLadderII() {
        println("=== Q4: Word Ladder II ===")
        println("Logic: BFS to find min distance map. DFS to collect paths.")
    }

    /**
     * 5. Course Schedule III
     * Logic: Sort by EndTime. Max Heap of durations. Swap if overrun.
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
     * Logic: Ratio sort. Max Heap.
     */
    @Test
    fun q6_minCostWorkers() {
        println("=== Q6: Min Cost K Workers ===")
        println("Logic: Sort by wage/quality. Iterate, keeping K smallest qualities via Max Heap.")
    }

    /**
     * 7. IPO
     * Logic: Two Heaps (Min Heap Capital, Max Heap Profit).
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
     * Logic: Two Heaps (Max Heap Lower, Min Heap Upper).
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
     * Logic: Dijkstra (Min Heap) on 2D Grid.
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
     * 10. Cut Off Trees
     * Logic: BFS step-by-step from min height tree to next min.
     */
    @Test
    fun q10_cutOffTrees() {
        println("=== Q10: Cut Off Trees ===")
        println("Logic: Sort trees by height. BFS dist between each ordered pair step.")
    }

    /**
     * 11. Shortest Path Grid Obstacles Elimination
     * Logic: BFS state (r, c, k_remaining).
     */
    @Test
    fun q11_shortestPathObstacles() {
        println("=== Q11: Shortest Path k Obstacles ===")
        println("Logic: BFS with visited state: visited[row][col] = max_k_remaining.")
    }

    /**
     * 12. Min Moves to Move Box
     * Logic: BFS state (box_r, box_c, player_r, player_c).
     */
    @Test
    fun q12_moveBox() {
        println("=== Q12: Move Box ===")
        println("Logic: Complex BFS. Check if player can reach 'push' position.")
    }

    /**
     * 13. Bus Routes
     * Logic: BFS on Routes (not Stops).
     */
    @Test
    fun q13_busRoutes() {
        println("=== Q13: Bus Routes ===")
        println("Logic: Graph routes are nodes. Intersecting routes have edges.")
    }

    /**
     * 14. Race Car
     * Logic: BFS (pos, speed).
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
                if(!visited.contains("$nPos,$nSpeed") && Math.abs(nPos - target) < target) {
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
     * Logic: BFS String state.
     */
    @Test
    fun q15_slidingPuzzle() {
        println("=== Q15: Sliding Puzzle ===")
        val board = "123450" // Target
        println("Logic: BFS states (string representation). Swap 0 with neighbors.")
    }

    /**
     * 16. Open the Lock
     * Logic: BFS '0000'.
     */
    @Test
    fun q16_openLock() {
        println("=== Q16: Open Lock ===")
        println("Logic: Bidirectional BFS faster. Avoid deadends.")
    }

    /**
     * 17. Min Jumps to Reach Home
     * Logic: BFS. State (index, canBack).
     */
    @Test
    fun q17_minJumpsHome() {
        println("=== Q17: Min Jumps Home ===")
        println("Logic: BFS on number line. Avoid forbidden.")
    }

    /**
     * 18. Min Flips Matrix Zero
     * Logic: BFS bitmask.
     */
    @Test
    fun q18_minFlipsMatrix() {
        println("=== Q18: Min Flips Matrix ===")
        println("Logic: 3x3 matrix fits in integer bitmask. BFS states.")
    }

    /**
     * 19. Closest Room
     * Logic: Sort + TreeSet.
     */
    @Test
    fun q19_closestRoom() {
        println("=== Q19: Closest Room ===")
        println("Logic: Sort rooms and queries by size. Add valid rooms to TreeSet. Find floor/ceiling.")
    }

    /**
     * 20. Max Number of Tasks Assign
     * Logic: Binary Search on Answer + Greedy (Deque).
     */
    @Test
    fun q20_maxTasksAssign() {
        println("=== Q20: Max Tasks Assign ===")
        println("Logic: Binary Search K. Check if feasible using Monotonic Deque.")
    }
}
