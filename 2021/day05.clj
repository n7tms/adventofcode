(ns aoc2021
  (:require [clojure.string :as string]))

(def day "05")
(def input-file (str "day" day "-input.txt"))


(def small "0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2")

(def large (slurp input-file))

;; globally switch between the small and large dataset.
;(def active small)
(def active large)


;; =================================================================
;;                              Pseudocode
;; take the inputs
;; ignore lines where x1 != x2 or y1 != y2
;; create vectors of each point in the line
;;    . (add vectors for positions implied in the line description)
;; count pairs where more than 1 exists
;; that should be the output of part 1.
;; =================================================================

(defn parse-line [line]
  (let [[_               x1    y1       x2    y2]
        (re-matches  #"(\d+),(\d+) -> (\d+),(\d+)" line)]
    [[x1 y1] [x2 y2]]))


(def parsed
  (->> (string/split-lines active)
       (map #(parse-line %))
       (flatten)
       (map #(Integer/parseInt %))
       (partition 4)
       ))


;; toint removes the diagonal lines
;; and puts the remaining into [[x1 y1] [x2 y2]] groups.
(def toint
  (remove nil?
          (for [x parsed]
            (let [x1 (nth x 0)
                  y1 (nth x 1)
                  x2 (nth x 2)
                  y2 (nth x 3)]
              (if (or (= x1 x2) (= y1 y2)) [[x1 y1] [x2 y2]] nil)))))


;; fill in the missing points between each given pair
(defn fill-gaps [pair]
  (remove empty?
          (conj pair 
                (let [x1 (first (first pair))
                      y1 (second (first pair))
                      x2 (first (second pair))
                      y2 (second (second pair))]
                  (if (= x1 x2)
                    (if (> y2 y1)
                      ;; fill y's
                      (for [i (range (inc y1) y2)]    [x1 i])
                      (for [i (range (dec y1) y2 -1)] [x1 i]))

                    ;; otherwise, fill x's
                    (if (> x2 x1)
                      (for [i (range (inc x1) x2)]    [i y1])
                      (for [i (range (dec x1) x2 -1)] [i y1]))
                    ))))
  )


;; take all the pairs (including gaps) and turn it into a list of pairs ((0 2) (1 3) ...)
(def all-points
  (partition
   2
   (flatten
    (map #(fill-gaps %) toint)))  )



(defn part1 []
  ;; produce the frequency of each pair...
  ;; filter out all the single pairs (those with no duplicates)
  ;; and count them.
  (count (filter (fn [[k v]] (> v 1)) (frequencies all-points)))
  )

(part1)   ;; => 5373
;; I totatlly forgot about the frequencies function. I spent FOREVER trying to remove the
;; points that were not duplicates. Solved part1 in 2:08:08.



(def toint2 ;; keep the diagonals
  (for [x parsed]
    (let [x1 (nth x 0)
          y1 (nth x 1)
          x2 (nth x 2)
          y2 (nth x 3)]
      [[x1 y1] [x2 y2]])))


;; like fill-gaps (above) but with diagonals.
;; this is much cleaner with (cond ..) construct.
(defn fill-gaps2 [pair]
  (remove empty?
          (conj pair 
                (let [x1 (first (first pair))
                      y1 (second (first pair))
                      x2 (first (second pair))
                      y2 (second (second pair))]

                  (cond
                    (and (= x1 x2) (> y2 y1)) (for [i (range (inc y1) y2)]    [x1 i])
                    (and (= x1 x2) (> y1 y2)) (for [i (range (dec y1) y2 -1)] [x1 i])
                    (and (= y1 y2) (> x2 x1)) (for [i (range (inc x1) x2)]    [i y1])
                    (and (= y1 y2) (> x1 x2)) (for [i (range (dec x1) x2 -1)] [i y1])
                    ;; diagonals
                    (and (> x2 x1) (> y2 y1)) (for [i (range 1 (- x2 x1))]  [(+ x1 i) (+ y1 i)])
                    (and (> x2 x1) (> y1 y2)) (for [i (range 1 (- x2 x1))]  [(+ x1 i) (- y1 i)])
                    (and (> x1 x2) (> y2 y1)) (for [i (range 1 (- x1 x2))]  [(- x1 i) (+ y1 i)])
                    (and (> x1 x2) (> y1 y2)) (for [i (range 1 (- x1 x2))]  [(- x1 i) (- y1 i)]))
))))


(def all-points2
  (partition
   2
   (flatten
    (map #(fill-gaps2 %) toint2)))  )


(defn part2 []
  (count (filter (fn [[k v]] (> v 1)) (frequencies all-points2)))
)

(part2)   ;; => 21514
;; Solved in 47 minutes.


(println "Part1: " (part1) "\nPart2: " (part2))


