(ns adventofcode
  (:require [clojure.string :as string]
            [clojure.test :as test]))

;; TODO: change 00 to current day number
(def day "00")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

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



(deftest sample-data-test
  (test/is (= (part1) 0))
  (test/is (= (part2) 0)))

(run-tests)

