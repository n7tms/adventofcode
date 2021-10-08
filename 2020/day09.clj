(ns adventofcode
  (:require [clojure.string :as str]))

;; Advent of Code 
;; Day 09
;;
;;


(def input-file "src/working/day09-input.txt")

(def sample
"35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576")

(defn parsed [x]
;  (Integer/parseInt x)
  (bigint x)
  )

;; (def the-numbers
;;   (->> (slurp input-file)
;;       str/split-lines
;;       (map parsed)
;;       ))

(def the-numbers
  (->> sample
       str/split-lines
       (map parsed)))

(take 5 (drop 1 the-numbers))

(defn sum [x]
  (let [addends (take 5 (drop (- x 5) the-numbers))]
    addends)
  )
(nth the-numbers 5)

(sum? 6)
(reduce (+ 64) sum)
