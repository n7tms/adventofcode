(ns aoc2015
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "02")
(def input-file (str "day" day "-input.txt"))

(def small "2x3x4
1x1x10")

(def large (slurp input-file))

;(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))

(def parsed
  (->> active
       (string/split-lines)))

(defn packages [line]
  (let [[_ l w h]
        (re-matches #"(\d+)x(\d+)x(\d+)" line)]
    [(Integer/parseInt l)
     (Integer/parseInt w)
     (Integer/parseInt h)])
  )


(defn part1 []
  (reduce +
          (for [p parsed]
            (let [box (packages p)
                  [l w h] box
                  s1 (first (sort box))
                  s2 (second (sort box))]
              (+ (* 2 l w) (* 2 w h) (* 2 h l) (* s1 s2))
              )
            ))
  )


(defn part2 []
  (reduce +
          (for [p parsed]
            (let [box (packages p)
                  [l w h] box
                  s1 (first (sort box))
                  s2 (second (sort box))]
              (+ (* 2 s1) (* 2 s2) (* l w h))
              )
            ))

  )



(println "Part1: " (part1) "\nPart2: " (part2))


