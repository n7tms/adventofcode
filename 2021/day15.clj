(ns aoc2021
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "15")
(def input-file (str "day" day "-input.txt"))

(def small "1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581")

(def smaller "123
456
789")


(def large (slurp input-file))

(def active smaller)
;(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))



(def parsed-input
  (vec
   (flatten
    (for [o 
          (->> active
               (string/split-lines)
               (map #(split #"" %)))]
      (map #(Integer/parseInt %) o)))))


(def width 3)
(def height 3)



;(def directions [(inc (- width)) 1 (inc width) width (dec width) -1 (dec (- width)) (- width)])
(def directions {:up (- width) :right 1 :down (+ width) :left -1})  ;; up right down left

(defn find-neighbors [col row]
  (let [idx     (+ (* row width) col)
        idx-key (keyword idx)
        up      (if (zero? row)          nil (+ idx (:up directions)))
        right   (if (= (inc col) width)  nil (+ idx (:right directions)))
        down    (if (= (inc row) height) nil (+ idx (:down directions)))
        left    (if (zero? col)          nil (+ idx (:left directions)))
        dirs    (remove nil? [up right down left])]

    (into {} (for [d dirs]   {(keyword (str d)) (nth parsed-input d)}))
;(for [d dirs] d)
    ))




(defn build-graph []
  (into {}
        (for [col (range width)
              row (range height)]
          (let [address (+ (* row width) col)
                neighbors (find-neighbors col row)]
            {(keyword (str address)) neighbors})))
  )

(def ^:private inf Double/POSITIVE_INFINITY)

(defn update-costs
  "Returns costs updated with any shorter paths found to curr's unvisisted
  neighbors by using curr's shortest path"
  [g costs unvisited curr]
  (let [curr-cost (get costs curr)]
    (reduce-kv
      (fn [c nbr nbr-cost]
        (if (unvisited nbr)
          (update-in c [nbr] min (+ curr-cost nbr-cost))
          c))
      costs
      (get g curr))))

(defn dijkstra
  "Returns a map of nodes to minimum cost from src using Dijkstra algorithm.
  Graph is a map of nodes to map of neighboring nodes and associated cost.
  Optionally, specify destination node to return once cost is known"
  ([g src]
    (dijkstra g src nil))
  ([g src dst]
    (loop [costs (assoc (zipmap (keys g) (repeat inf)) src 0)
           curr src
           unvisited (disj (apply hash-set (keys g)) src)]
      (cond
       (= curr dst)
       (select-keys costs [dst])

       (or (empty? unvisited) (= inf (get costs curr)))
       costs

       :else
       (let [next-costs (update-costs g costs unvisited curr)
             next-node (apply min-key next-costs unvisited)]
         (recur next-costs next-node (disj unvisited next-node)))))))


(defn part1 []
  (dijkstra (build-graph) :0 :9999))  ;; => 363 in ~15 sec


(defn part2 []

  )



;(println "Part1: " (part1) "\nPart2: " (part2))



;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (test/is (= (part1) xx))
;;   (test/is (= (part2) xx)))

;; (run-tests)

;; Algorithm here: http://www.loganlinn.com/blog/2013/04/22/dijkstras-algorithm-in-clojure/



parsed-input


;; 1 2 3   2 3 4   3 4 5
;; 4 5 6   5 6 7   6 7 8
;; 7 8 9   8 9 1   9 1 2
;;
;; 2 3 4   3 4 5   4 5 6
;; 5 6 7   6 7 8   7 8 9
;; 8 9 1   9 1 2   1 2 3
;;
;; 3 4 5   4 5 6   5 6 7
;; 6 7 8   7 8 9   8 9 1
;; 9 1 2   1 2 3   2 3 4

(defn init-matrix [newsize-c newsize-r]
  (repeat (* newsize-c newsize-r) 0))

(def old-matrix parsed-input)     ;; old matrix
(def new-matrix (init-matrix 6 6)) ;; new matrix, init'd with 0's
;(def new-matrix [1 2 3 0 0 0 4 5 6 0 0 0 7 8 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0])
(loop [col 0
       row 0
       idx (+ (* row width) col)
       n-m new-matrix]
  (if (= col width) n-m
      (update n-m idx (inc (nth old-matrix idx))))
  
  )



;; fwiw: https://github.com/theronic/advent-of-code/blob/main/src/advent_of_code/year2021/day15.clj

;; def make_big(aoc_input):
;;     h,w=len(aoc_input),len(aoc_input[0])
;;     for i in range(h):
;;         for p in range(w,5*w):
;;             aoc_input[i].append(aoc_input[i][p-w]%9+1)
;;     for i in range(h,5*h): 
;;         aoc_input.append([aoc_input[i-h][p]%9+1 for p in range(5*w)])

(defn make-bigger [parsed-input]
  (for [r (range height)
        c (range width (* 5 width))]
    (update parsed-input i (inc (mod (- c width) 9))))
)

()
