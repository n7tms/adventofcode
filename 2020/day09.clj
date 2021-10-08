(ns adventofcode
  (:require [clojure.string :as str]))

;; Advent of Code 
;; Day 09
;;
;;  .... with help from Fred Overflow
;;  .... https://www.youtube.com/watch?v=61ksBHphf4s&list=PLbPrugU2oQ8VURsQdZ6W_iovXRS24UmZQ&index=9


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
  (Long/parseLong x)
  )

(def the-numbers
  (->> (slurp input-file)
      str/split-lines
      (mapv parsed)
      ))

;; (def the-numbers
;;   (->> sample
;;        str/split-lines
;;        (mapv parsed)))

(take 5 (drop 1 the-numbers))

(defn sum? [x]
  (let [addends (take 5 (drop (- x 5) the-numbers))]
    addends)
  )
(nth the-numbers 5)

(sum? 6)
the-numbers

(defn sum-pair [numbers target]
  (let [n (count numbers)]
    (first  (for [i (range (dec n))
                  j (range (inc i) n)
                  :when (= target (+ (numbers i) (numbers j)))]  [i j]))))


(sum-pair (subvec the-numbers 1 6) 62)

(defn invalid [n numbers]
  (first (for [i (range (- (count numbers) n))
               :let [k (+ i n)
                     window (subvec numbers i k)
                     target (numbers k)]
               :when (not (sum-pair window target))]
           target)))

(defn part1 []
  (invalid 25 the-numbers))

(part1)   ;; => 26796446



(defn summands [n numbers]
  (loop [i 0
         k 0
         s (- (invalid n numbers))]
    (cond
      (neg? s) (recur i (inc k) (+ s (numbers k)))
      (pos? s) (recur (inc i) k (- s (numbers i)))
      :otherwise
      (sort (subvec numbers i k)))
    ))


(defn part2 [n numbers]
  (let [a (first (summands n numbers))
        b (last (summands n numbers))]
    (+ a b)))



(part2 25 the-numbers)  ;; => 3353494


