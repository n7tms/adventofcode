(ns aoc2021
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "07")
(def input-file (str "day" day "-input.txt"))

(def small "16,1,2,0,4,2,7,1,2,14")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(def fuels
  (->> active
       (string/trim)
       (split #",")
       (map #(Integer/parseInt %))
       ))

;; ===================================
;; Strategy
;;
;; what is the max and min fuel level?
;; average? frequencies?
;; There is probably a statistical function to find
;; for loop...iterate through the fuels 0 - 1911
;; - use `map` to calc the distance
;; - sum the distances
;; return the lowest distance (first (sort %))
;; 


;; for part1, each move is worth 1 fueld
(defn calc-diff [pos mover]
  (cond
    (= pos mover) 0
    (> pos mover) (- pos mover)
    (< pos mover) (- mover pos)))

;; for part2, 1st move is 1 fuel, 2nd move is 2 fuel, etc.
;; calc the diff between the two positions, define a range of 1 to the diff,
;; and sum the range.
(defn calc-diff2 [pos mover]
  (cond
    (= pos mover) 0
    (> pos mover) (let [offset (inc (- pos mover))] (apply + (range 1 offset)))
    (< pos mover) (let [offset (inc (- mover pos))] (apply + (range 1 offset)))))



(defn part1 []
  (first
   (sort
    (for [f fuels]
      (let [fc (map #(calc-diff f %) fuels)]
        (reduce + fc))
      )))
  ) ;; => 343605 ~28 minutes

;; I'm iterating through the fuels, but there are some numbers (optimum numbers)
;; missing ... e.g. "5" in the sample data.
;; So, in part2, I created a range from 0 to the max number and iterated over
;; the range so as not to miss any values.
(defn part2 []
  (first
   (sort
    (for [f (range 0 (last (sort fuels)))]
      (let [fc (map #(calc-diff2 f %) fuels)]
        (reduce + fc))) ))
  )  ;; => 96744904 ~58 minutes


(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (test/is (= 37 (part1)))
  (test/is (= 168 (part2)))
  )

(run-tests)

