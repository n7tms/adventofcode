(ns adventofcode
  (:require [clojure.string :as string]))

(def day "04")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "1000-1555")
(def large "271973-785961")

;(def active small)
(def active large)

(def input
  (let [[_ start end]
        (re-matches #"(\d+)-(\d+)" active)]
    [(Integer/parseInt start) (Integer/parseInt end)]))

(defn to-digits [number] (map #(Character/digit % 10) (str number)))

;; are the digits increasing in value?
(defn increasing? [number]
  (apply <= (to-digits number)))

;; is there a pair of digits?
(defn adjacent? [number]
  (< (count (partition-by identity (to-digits number))) 6))

;; is there one or more (at-least 1) adjacent pair? (for part2)
(defn al1-adjacent? [number]
  (some #(= (count %) 2) (partition-by identity (to-digits number))))


(defn part1 []
  (let [mn (first input)
        mx (last input)]
    (count
     (filter true?
             (for [x (range mn mx)]
               (and
                (increasing? x)
                (adjacent? x)))))))


(defn part2 []
  (let [mn (first input)
        mx (last input)]
    (count
     (filter true?
             (for [x (range mn mx)]
               (and
                (increasing? x)
                (al1-adjacent? x)))))))


(println "AoC 2019 - Day 4")
(time (println "Part1: " (part1)))   ;; => 925
(time (println "Part2: " (part2)))   ;; => 607





