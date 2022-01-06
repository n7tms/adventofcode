(ns adventofcode
  (:require [clojure.string :as string]))

(def day "06")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(defn parse-orbits [orbit]
  (let [[_ obj1 obj2]
        (re-matches #"(.+)\)(.+)" orbit)]
    {(keyword obj1) [(keyword obj2)]}))


(def input
  (merge-with into
              (->> active
                   string/split-lines
                   (map parse-orbits)

                   ))
  )


(defn visited? [v coll] (some #(= % v) coll))

(defn find-neighbors [v coll] (get coll v))

(defn graph-dfs
  "Traverses a graph in Depth First Search (DFS)"
  [graph v]
  (loop [stack   (vector v) ;; Use a stack to store nodes we need to explore
         visited []]        ;; A vector to store the sequence of visited nodes
    (if (empty? stack)      ;; Base case - return visited nodes if the stack is empty
      visited
      (let [v           (peek stack)
            neighbors   (find-neighbors v graph)              ;; right branch first
;            neighbors   (-> (find-neighbors graph v) reverse) ;; left branch first
            not-visited (filter (complement #(visited? % visited)) neighbors)
            new-stack   (into (pop stack) not-visited)]
        (if (visited? v visited)
          (recur new-stack visited)
          (recur new-stack (conj visited v)))))))

(defn graph-bfs
  "Traverses a graph in Breadth First Search (BFS)."
  [graph v]
  (loop [queue   (conj clojure.lang.PersistentQueue/EMPTY v) ;; queue to store explored nodes
         visited []]                                         ;; A vector of visited sequence
    (if (empty? queue) visited                               ;; Base case - return visited nodes if the queue is empty
        (let [v           (peek queue)
              neighbors   (find-neighbors v graph)
              not-visited (filter (complement #(visited? % visited)) neighbors)
              new-queue   (apply conj (pop queue) not-visited)]
          (if (visited? v visited)
            (recur new-queue visited)
            (recur new-queue (conj visited v)))))))



(defn keys-in
  "Returns a sequence of all key paths in a given map using DFS walk."
  [m]
  (letfn [(children [node]
            (let [v (get-in m node)]
              (if (map? v)
                (map (fn [x] (conj node x)) (keys v))
                [])))
          (branch? [node] (-> (children node) seq boolean))]
    (->> (keys m)
         (map vector)
         (mapcat #(tree-seq branch? children %)))))

(keys-in input)

(find-neighbors :G input)


(graph-bfs input :COM)

(let [obj1 "B"
      obj2 "C"]
  {(keyword obj1) [(keyword obj2)]})

(defn part1 []

  )




(defn part2 []

  )




;(println "Part1: " (part1) "\nPart2: " (part2))





