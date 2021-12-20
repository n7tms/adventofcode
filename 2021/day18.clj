(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.test :as test]))


(def day "18")
(def input-file (str "day" day "-input.txt"))

(def small "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]")


(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(def line1 ["[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]"])

(def input
  (->> small
       string/split-lines
       (mapv clojure.edn/read-string)))




(defn add-pair [])
(defn reduce-pair [])
(defn explode-pair [])
(defn split-pair [])
(defn magnitude [])






(defn part1 []

  )




(defn part2 []

  )



(println "Part1: " (part1) "\nPart2: " (part2))



(deftest sample-data-test
  (test/is (= (part1) 4140 ))
  (test/is (= (part2) 00)))

(run-tests)

