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

(def large (slurp input-file))

;(def active small)
(def active large)

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


(def width 100)
(def height 100)



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



(def demo-graph {:red    {:green 10, :blue   5, :orange 8},
                 :green  {:red 10,   :blue   3},
                 :blue   {:green 3,  :red    5, :purple 7},
                 :purple {:blue 7,   :orange 2},
                 :orange {:purple 2, :red    2}})
;                 :blue   {:green 3,  :red    5, :purple 7},



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



(println "Part1: " (part1) "\nPart2: " (part2))


;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (test/is (= (part1) xx))
;;   (test/is (= (part2) xx)))

;; (run-tests)

;; Algorithm here: http://www.loganlinn.com/blog/2013/04/22/dijkstras-algorithm-in-clojure/
