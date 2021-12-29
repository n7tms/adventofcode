;; Advent of Code 2020
;; Day 1
;;
;; https://adventofcode.com/2020/day/1

(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; This is the input data we'll be using.
;; The full path follows. 
;(def input-file "/home/todd/dev/clojure/adventofcode/2020/day01-input.txt")
;; This uncommented line assumes the input file is in the same directory as the .clj
(def input-file "day01-input.txt")

;; This slurp line is just for testing. (Can I read the input file?)
;(slurp input-file)

;; Read the input file and parse it into a 
(defn read-numbers [input]
  (->> input
       slurp                         ; read the file (provided in "input") (slurp reads the entire file into memory.)
       str/split-lines               ; segregate the numbers into a vector of strings
       ;; Integer/parseInt is a java function wrapped "#()" into a Clojure object.
       ;; The % is a placeholder for the string/number being "looked at".
       (map #(Integer/parseInt %)))) ; convert the number-strings to a set of numbers.

;; Part 1
;; Find the TWO numbers in the input-file that sum to 2020
(defn part1 []
  (let [numbers (read-numbers input-file)]
    (for [a numbers
          b numbers
          :when (< a b)             ; This :when assures that only one set is returned.
          :when (= 2020 (+ a b))]
      (* a b) )))

;; Part 2
;; Find the THREE numbers in the input-file that sum to 2020
(defn part2 []
  (let [numbers (read-numbers input-file)]
    (for [a numbers
          b numbers
          c numbers
          :when (< a b c)           ; Like above, this :when assures that only one set is returned.
          :when (= 2020 (+ a b c))]
      (* a b c) )))


(part1)     ; =>   1015476
(part2)     ; => 200878544
