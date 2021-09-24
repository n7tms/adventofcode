(ns aoc2020.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; Advent of Code 2020
;; Day 5

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

(Integer/parseInt "0101100101" 2)  ; = 357

(defn parse-seat-id [seat-id]
  (-> seat-id
      (str/replace #"F|L" "0")
      (str/replace #"B|R" "1")
      (Integer/parseInt 2)))

(parse-seat-id "FBFBBFFRLR")

(with-open [rdr (io/reader "/home/todd/dev/clojure/aoc2020/puzzlefiles/day5.txt")]
  (->> rdr
       line-seq
       (map parse-seat-id)
;       vec
       
       ; Part 1 - Find the max ID   ; = 896
       ;(apply max) 
       
       ; Part 2 - Find own ID  ; = 659
       sort
       (partition 2 1)
       (filter (fn [[x z]] (= (inc x) (dec z))))
       first  ;sole pair
       first  ;x
       inc    ;x+1
))



(->> "/home/todd/dev/clojure/aoc2020/puzzlefiles/day5.txt"
     slurp)
