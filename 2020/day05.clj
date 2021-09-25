;; Advent of Code 2020
;; Day 5
;;
;; https://adventofcode.com/2020/day/5

(ns aoc2020.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; Sample Data
; BFFFBBFRRR: row 70, column 7, seat ID 567.
; FFFBBBFRRR: row 14, column 7, seat ID 119.
; BBFFBBFRLL: row 102, column 4, seat ID 820.

; Interpretation
; row----col
; FBFBBFFRLR
; 0101100101       = 357
; |||||||||1
; ||||||||2
; |||||||4
; ||||||8
; |||||16
; ||||32
; |||64
; ||128
; |256
; 512

;; convert a binary number to a decimal integer (just testing)
(Integer/parseInt "0101100101" 2)  ; = 357

(def input-file "day05-input.txt")

;; When you break down the problem, the F and L characters represent 0 and the B and R charaters represent 1.
(defn parse-seat-id [seat-id]
  (-> seat-id
      (str/replace #"F|L" "0")     ;; replace all the F and L letters with a 0
      (str/replace #"B|R" "1")     ;; replace all the B and R letters with a 1
      (Integer/parseInt 2)))       ;; convert the resulting binary number to a decimal number.

;; Given a known value, test the parse-seat-id function
(parse-seat-id "FBFBBFFRLR")  ;; => 357


;; Part 1 - Find the max ID
(defn part1 []
(with-open [rdr (io/reader input-file)]
  (->> rdr
       line-seq
       (map parse-seat-id)
;       vec
       (apply max) 
)))

;; Part 2: Find my own ID
(defn part2 []
  (with-open [rdr (io/reader input-file)]
    (->> rdr
         line-seq
         (map parse-seat-id)
         sort
         (partition 2 1)
         (filter (fn [[x z]] (= (inc x) (dec z))))
         first  ;sole pair
         first  ;x
         inc    ;x+1
         ))
)


(part1)   ;; => 896
(part2)   ;; => 659

