(ns aoc2021
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "07")
(def input-file (str "day" day "-input.txt"))

(def small "")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))



(defn part1 []

  )




(defn part2 []

  )



(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (test/is (= (part1) xx))
  (test/is (= (part2) xx)))

(run-tests)

