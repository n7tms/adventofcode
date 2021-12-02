;; Advent of Code
;; Day 01 - Sonar Sweep
;;
(ns aoc
  (:require [clojure.string :as str]))

(def large (slurp "day01-input.txt"))

(def small "199
200
208
210
200
207
240
269
260
263")

;; Parse the input by splitting it into separate lines and
;; converting each term to an integer.
(def parsed 
  (->> large
       (str/split-lines)
       (map #(Integer/parseInt %))))


;; Partition the input (eg. (199 200 208 210 ... ) => ((199 200) (200 208) (208 210) ...) )
;; then iterate through each of the pairs comparing. Return a 1 if last if greater than first.
;; Otherwise a 0. Add up all the 1's. That how many depth increases exists.
(defn part1 []
  (apply +
         (for [x (partition 2 1 parsed)]
           (if (> (last x) (first x)) 1 0)
           )))

;; This clever snippet came from Gavin Sinclair (https://www.youtube.com/watch?v=Vp1wK1YS_9Y)
(->> parsed
     (partition 2 1)
     (filter (fn [[a b]] (> b a)))
     count)

(part1)  ;; => 1696


;; Same as part 1, only instead of comparing initial pairs, we are going to compare running
;; sums of 3. (eg. ((199 200 208 210 ... ) => ((199 200 208) (200 208 210)) => ((607 618) ...)
(defn part2 []
  (apply + 
         (for [x (partition 2 1  ;; partition running sums into groups of 2
                            (for [x (partition 3 1 parsed)] ;; sum of groups of three
                              (apply + x)))]
           (if (> (last x) (first x)) 1 0))))


;; I appied Gavin's algorithm from part 1 and solved part 2.
(->> parsed
     (partition 3 1)
     (map #(reduce + %))
     (partition 2 1)
     (filter (fn [[a b]] (> b a)))
     count
)

(part2)   ;; => 1737

