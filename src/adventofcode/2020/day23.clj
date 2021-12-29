;; Advent of Code
;; Day 23 - Crab Cups
;;
;; Kudos to Fred Overflow - https://www.reddit.com/r/Clojure/comments/kpo2wt/advent_of_code_2020_day_23_in_clojure_again/
;;

(ns adventofcode
  (:require [clojure.string :as string]))

(def small [3 8 9 1 2 5 4 6 7])
(def large [6 5 3 4 2 7 9 1 8])

(defn cups->circle ^longs [cups-prefix, ^long total-cups]
  (let [circle (long-array (inc total-cups))]
    (doseq [[a b] (partition 2 1 cups-prefix)]
      (aset-long circle a b))
    
    (loop [a (peek cups-prefix)
          b (inc (count cups-prefix))]
      (if (<= b total-cups)
        (do
          (aset-long circle a b)
          (recur b (inc b)))
        (aset-long circle a (first cups-prefix))))
    circle))


(defn destination [^longs circle, ^long current, ^long x, ^long y]
  (let [destination (dec (if (= 1 current)
                          (long (alength circle))
                          current))]
    (if (or
          (= x destination)
          (= y destination)
          (= (aget circle y) destination))
      (recur circle destination x y)
      destination)))

(defn play-crab-cups [^longs circle, ^long current, ^long steps]
  (if (zero? steps)
    circle
    (let [x (aget circle current)
          y (aget circle x)
          z (aget circle y)
          next (aget circle z)
          destination (destination circle current x y)]
      
      (aset-long circle current next)
      (aset-long circle z (aget circle destination))
      (aset-long circle destination x)
      
      (recur circle next (dec steps)))))


; part 1

(let [circle (cups->circle large 9)]
  (play-crab-cups circle (first large
                          ) 100)
  (->> (aget circle 1)
    (iterate (partial aget circle))
    (take 8)
    string/join))   ;; => 76952348


; part 2

(let [circle (time (cups->circle large 1000000))]
  (time (play-crab-cups circle (first large) 10000000))
  (let [a (aget circle 1)
        b (aget circle a)]
    (* a b)))  ;; => 72772522064
