(ns aoc2021
  (:require [clojure.string :as string]))


(def day "06")
(def input-file (str "day" day "-input.txt"))

(def small "3,4,3,1,2")
(def large "1,4,2,4,5,3,5,2,2,5,2,1,2,4,5,2,3,5,4,3,3,1,2,3,2,1,4,4,2,1,1,4,1,4,4,4,1,4,2,4,3,3,3,3,1,1,5,4,2,5,2,4,2,2,3,1,2,5,2,4,1,5,3,5,1,4,5,3,1,4,5,2,4,5,3,1,2,5,1,2,2,1,5,5,1,1,1,4,2,5,4,3,3,1,3,4,1,1,2,2,2,5,4,4,3,2,1,1,1,1,2,5,1,3,2,1,4,4,2,1,4,5,2,5,5,3,3,1,3,2,2,3,4,1,3,1,5,4,2,5,2,4,1,5,1,4,5,1,2,4,4,1,4,1,4,4,2,2,5,4,1,3,1,3,3,1,5,1,5,5,5,1,3,1,2,1,4,5,4,4,1,3,3,1,4,1,2,1,3,2,1,5,5,3,3,1,3,5,1,5,3,5,3,1,1,1,1,4,4,3,5,5,1,1,2,2,5,5,3,2,5,2,3,4,4,1,1,2,2,4,3,5,5,1,1,5,4,3,1,3,1,2,4,4,4,4,1,4,3,4,1,3,5,5,5,1,3,5,4,3,1,3,5,4,4,3,4,2,1,1,3,1,1,2,4,1,4,1,1,1,5,5,1,3,4,1,1,5,4,4,2,2,1,3,4,4,2,2,2,3")

;(def active small)
(def active large)

(defn split [regex s]
  (string/split s regex))

(def parsed (mapv #(Integer/parseInt %) (split #"," active)))


;; My initial part1 solution ......
;; (defn next-day [fish]
;;   (vec (flatten
;;         (for [x fish]
;;           (if (= 0 x) [6 8] (dec x)))))
;;   )

;; a for loop is much too slow for 256 days. 
;; Instead, I am going to keep track of the categories of fish...
;; in other words, I'll keep track of how many fish are at day 1, and day 4, and day 5, etc.


;; I initilized the buckets manually by producing a frequency of the input.
(def buckets
  (hash-map :0 0, :1 80, :2 53, :3 51, :4 63, :5 53, :6 0, :7 0, :8 0)
)

;; (def buckets
;;   (hash-map :0 0, :1 1, :2 1, :3 2, :4 1, :5 0, :6 0, :7 0, :8 0)
;; )


;; Takes the value from the one-higher index and moves it to a lower index.
;; :8's are the number of :0's (birthed).
;; :6's are the number of :7's plus the number of :0's
(defn dec-bucket [buck]
  (let [new-buck buck
        birthed (:0 buck)]
    (hash-map
     :0 (:1 buck)
     :1 (:2 buck)
     :2 (:3 buck)
     :3 (:4 buck)
     :4 (:5 buck)
     :5 (:6 buck)
     :6 (+ (:7 buck) birthed)
     :7 (:8 buck)
     :8 birthed)
    ))

;; how many fish in 80 days?
(defn part1 []
  (loop [day 0
         bucks buckets]
    (if (= day 80)
      (apply + (vals bucks))
      (recur (inc day) (dec-bucket bucks)))))


;; how many fish in 256 days?
(defn part2 []
  (loop [day 0
         bucks buckets]
    (if (= day 256)
      (apply + (vals bucks))
      (recur (inc day) (dec-bucket bucks)))))


(println "Part1: " (part1) "\nPart2: " (part2))





