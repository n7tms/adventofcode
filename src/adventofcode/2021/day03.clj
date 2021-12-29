(ns aoc2021
  (:require [clojure.string :as string]))


(def day "03")
(def input-file (str "day" day "-input.txt"))

(def small "00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010")

(def large (slurp input-file))

(defn split [regex s]
  (string/split s regex))

(defn to-digits [num]
  (map #(Character/getNumericValue %) num))


;; There are 1000 entries in the large dataset.
;; If there are more than 500 1's then there is
;; a majority of 1's -- otherwise return a 0.
(defn majority [x]
  (if (> x 500) 1 0))

;; This majority function is used to against the 
;; small dataset of 12 entries.
;; (defn majority [x]
;;   (if (>= x 6) 1 0))

(def gamma
  (->> large
       (string/split-lines)
       (map #(to-digits %))
       (reduce (partial map + ))
       (map #(majority %))
       (string/join)
       )
)


(defn invert-number [n] (if (= n "1") "0" "1"))

(defn number-complement [num]
  (let [numstr (split #"" (Integer/toString num 2))
        bits (map invert-number numstr)
        complement-bits (string/join bits)]
    (Integer/parseInt complement-bits 2)))


(defn part1 []
  (let [g (Integer/parseInt gamma 2)
        e (bit-xor g 2r111111111111)]
    (* g e))
  )

(part1)  ;; => 3633500

;; ==============================================================
;; Part 2
;;
;; I discovered between part 1 and now that clojure has bit-wise
;; operations. Who knew! It sure made this second part much 
;; easier/cleaner.
;; ==============================================================


(def smaller
  (->> small
       (string/split-lines)
       (map #(Integer/parseInt % 2))
       ))

(def larger
  (->> large
       (string/split-lines)
       (map #(Integer/parseInt % 2))
       )  )


(defn majority2 [mask coll]
  (if (>= (count
           (filter #(= mask (bit-and mask %)) coll))
          (/ (count coll) 2))
    1 0)
  )


(defn o2 [mask coll]
  (if (zero? (majority2 mask coll))
    ;; more 0's
    (remove #(= mask (bit-and mask %)) coll)

    ;; more 1's
    (filter #(= mask (bit-and mask %)) coll)
    ))

(defn co2 [mask coll]
  (if (zero? (majority2 mask coll))
    ;; more 0's
    (filter #(= mask (bit-and mask %)) coll)

    ;; more 1's
    (remove #(= mask (bit-and mask %)) coll)
    ))



(defn find-o2 []
  (loop [x 2048
         coll larger]
    (let [newcoll (o2 x coll)]
      (if (= 1 (count newcoll))
        (first newcoll)
        (recur (/ x 2) newcoll))))
  )

(defn find-co2 []
  (loop [x 2048
         coll larger]
    (let [newcoll (co2 x coll)]
      (if (= 1 (count newcoll))
        (first newcoll)
        (recur (/ x 2) newcoll))))
  )

(defn part2 []
  (* (find-o2) (find-co2))
  )

(part2)   ;; => 4550283


