(ns aoc2021
  (:require [clojure.string :as str]
            [clojure.set :as set]))

;; Solution courtesy of https://github.com/RedPenguin101/aoc2021/blob/main/day12.md
;;
;; (rearranged and formatted to my style)

(def small "start-A
start-b
A-c
A-b
b-d
A-end
b-end")

(def large (slurp "day12-input.txt"))

;; creates a list of nodes and their reverse, ie. A-c and c-A ["A" "c"] ["c" "A"]
(def graph
  (mapcat #((juxt identity reverse) (str/split % #"-"))
          (str/split-lines small)))

;; returns a hash map of the nodes accessible from the given "node"
(defn children [graph node]
  (set (keep #(when (= node (first %)) (second %)) graph)))

;; returns true if the given string is all lowercase letters
(defn lower-case? [string]
  (= (str/lower-case string) string))

(defn with-no-lc-revisits [graph node path]
  (set/difference (children graph node) (set (filter lower-case? path))))

(defn with-one-lc-revisit [graph node path]
  (if ((set (vals (frequencies (filter lower-case? path)))) 2)
    (with-no-lc-revisits graph node path)
    (disj (children graph node) "start")))

(defn all-paths 
  ([next-fn graph] (all-paths next-fn '("start") "end" graph))
  ([next-fn path to graph]
   (if (= (first path) to) (list path)
       (mapcat #(all-paths next-fn (conj path %) to graph)
               (next-fn graph (first path) path)))))


;(time (count (all-paths with-no-lc-revisits graph)))
  ;; Elapsed time: 297 msecs
  ;; => 3292


;(time (count (all-paths with-one-lc-revisit graph)))

;; => Elapsed time: 11596 msecs
;; => 89592
  
