;; Advent of Code 2019
;; --- Day 1: The Tyranny of the Rocket Equation ---
;;

(ns adventofcode
  (:require [clojure.string :as string]))


(def day "01")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))


(def large (slurp input-file))
(def small "100756")  ;; a test input

;(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))

;; Recursively divide by 3 and subtract 2 until less than or equal to 0
;; return the sum of the recursive calculations.
(defn calc-fuel [starting-fuel]
  (loop [fuel starting-fuel
         sum 0]
    (let [new-fuel (- (quot fuel 3) 2)]
      (if (<= new-fuel 0)
        sum
        (recur new-fuel (+ sum new-fuel))))))


(defn part1 []
  (->> active
       string/split-lines
       (map #(Integer/parseInt %))
       (map #(- (quot % 3) 2))
       (reduce +)
       ))


(defn part2 []
  (->> active
       string/split-lines
       (map #(Integer/parseInt %))
       (map #(calc-fuel %))
       (reduce +)
       )
  )

(part1)  ;; => 3256794
(part2)  ;; => 4882337


(println "Part1: " (part1) "\nPart2: " (part2))




