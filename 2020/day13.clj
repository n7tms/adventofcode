;; Advent of Code
;; Day 13
;;
;; I found a solution to part 1 comletely on my own. 
;; I did play with Fred's code after the fact in an effort to optimize my own.
;;

(ns adventofcode
  (:require [clojure.string :as string]))


(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (string/split s regex))

(defn indices [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))


(def small-leave 939)
(def small-bus "7,13,x,x,59,x,31,19")

(def large-leave 1000434)
(def large-bus "17,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,983,x,29,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,19,x,x,x,23,x,x,x,x,x,x,x,397,x,x,x,x,x,37,x,x,x,x,x,x,13")

(def leave large-leave)
(def bus large-bus)

(defn not-x? [item]
  (not= item "x"))

(defn next-leave [bus]
  (last (range bus (+ leave bus) bus))
  )

(def next-leave-time
  (->> bus
       (split #",")
       (filter  not-x?)
       (mapv #(Integer/parseInt %))
       (mapv #(next-leave %))
;       (apply min)
       ))


(def busses
  (->> bus
       (split #",")
       (filter not-x?)
       clojure.walk/keywordize-keys
       (map #(Integer/parseInt %))
  )
)

(defn part1 []
  (*
   (nth busses (first (indices #(= (apply min next-leave-time) %) next-leave-time))) 
   (- (apply min next-leave-time) leave))
)

(part1)   ;; => 2382


;; ==================================================
;; Fred's code
;;
(def f-small "939
7,13,x,x,59,x,31,19")

(def f-large (slurp "day13-input.txt"))

(defn f-part1 []
  (let [[earliest & bus-ids]
        (->> f-large
             (re-seq #"\d+")
             (map #(Integer/parseInt %)))]
    (->> bus-ids
         (map (fn [bus-id]
                [(- bus-id (mod earliest bus-id))
                 bus-id]))
         (apply min-key first)
         (reduce *))
    )) 

;; ========== Playing with code ============
(let [[earliest & bus-ids]              ;; binds earliest to 939 and bus-ids to the rest
      (->> f-small                      ;; => ("939\n7,13,x,x,59,x,31,19")
           (re-seq #"\d+")              ;; => ("939" "7" "13" "59" "31" "19")
           (map #(Integer/parseInt %))  ;; => (939 7 13 59 31 19)
           )]
  (->> bus-ids                          ;; then we can do something with just bus-ids
       (map (fn [bus-id]
              [(- bus-id (mod earliest bus-id))
               bus-id]))               ;; => ([6 7] [10 13] [5 59]...) [minutes-after bus-id]
       (apply min-key first)           ;; returns pair where first num in pair is min of all first nums
       (reduce *)                      ;; multiply the two numbers in the pair together.
       )
)

;; We're ready to depart 939 minutes after midnight.
;; One of the busses departs every 7 minutes
;; 939 % 7 = a bus left 1 minute ago
;; 7 - 1 = next bus will depart 6 minutes from now
;; generalized form: (- bus-id (mod earliest bus-id))

(mod 939 7)
(- 7 (mod 939 7))
;; =========================================





(f-part1)   ;; => 2382


;; part 2
;; sum = 0
;; keep adding 7          until sum+1 divisible by 13
;; keep adding 7*13       until sum+4 divisible by 59
;; keep adding 7*13*59    until sum+6 divisible by 31
;; keep adding 7*13*59*31 until sum+7 divisible by 19
;;             product            index           prime
;;

(defn magic [[sum product] [index prime]]
  (loop [sum sum]
    (if (zero? (mod (+ sum index) prime))
      [sum (* product prime)]
      (recur (+ sum product)))))

(magic [77 (* 7 13)] [4 59])

(defn f-part2 []
  (->> f-large
       (re-seq #"x|\d+")
       rest
       (interleave (range))
       (partition 2)
       (map (fn [[index prime]]
              (if (not= "x" prime)
                [index (Integer/parseInt prime)]))
            )
       (remove nil?)
       (reduce magic)
       first
       ))

(f-part2)   ;; => 906332393333683


