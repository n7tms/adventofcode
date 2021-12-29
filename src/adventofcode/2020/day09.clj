(ns adventofcode
  (:require [clojure.string :as str]))

;; Advent of Code 
;; Day 09
;;
;;  .... with help from Fred Overflow
;;  .... https://www.youtube.com/watch?v=61ksBHphf4s&list=PLbPrugU2oQ8VURsQdZ6W_iovXRS24UmZQ&index=9


;(def input-file "src/working/day09-input.txt")   ;; at CoR
(def input-file "day09-input.txt")   ;; at home

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


(defn sum-pair [numbers target]
  (let [n (count numbers)]
    (first  (for [i (range (dec n))
                  j (range (inc i) n)
                  :when (= target (+ (numbers i) (numbers j)))]  [i j]))))

;(sum-pair (subvec the-numbers 1 6) 62)

;; searches a window of n items looking for two that sum to
;; the n+1 number. This fn returns the first item (number)
;; that does not have a pair in the window.
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



;; This is interesting logic to me...
;; It finds the invalid number and then makes it negative (s).
;; Then it adds consecutive numbers until the number goes positive
;; When it finds the right sequence of numbers, it returns a sorted
;; vector.
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

;; This calls summands to get the sorted vector list.
;; Once it has it, it adds the first and last number
;; and returns the answer!
(defn part2 [n numbers]
  (let [a (first (summands n numbers))
        b (last (summands n numbers))]
    (+ a b)))

(part2 25 the-numbers)  ;; => 3353494


