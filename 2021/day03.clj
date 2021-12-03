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

;; (defn majority [x]
;;   (if (> x 500) 1 0))

(defn majority [x]
  (if (>= x 6) 1 0))

(def gamma
  (->> small
       (string/split-lines)
       (map #(to-digits %))
       (reduce (partial map + ))
       (map #(majority %))
       (string/join)
       )
)

gamma

(defn invert-number [n] (if (= n "1") "0" "1"))

(defn number-complement [num]
  (let [numstr (split #"" (Integer/toString num 2))
        bits (map invert-number numstr)
        complement-bits (string/join bits)]
    (Integer/parseInt complement-bits 2)))

(number-complement 23)

(defn part1 []
  (let [g (Integer/parseInt gamma 2)
        e (bit-xor g 2r111111111111)]
    (* g e))
  )

(part1)  ;; => 3633500

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

(def width 5)

(defn power
  "raise 'base' to the power of 'exp'"
  [base exp]
  (reduce * (repeat exp  base))
  )

(defn majority2 [mask coll]
  (if (>= (count
           (filter #(= mask (bit-and mask %)) coll))
          (/ (count coll) 2))
    1 0)
  )

(def small-mask [16 8 4 2 1])
(def large-mask [2048 1024 512 256 128 64 32 16 8 4 2 1])

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


(o2 1 (o2 2 (o2 4 (o2 8 (o2 16 smaller)))))  ;; =>23
(co2 1 (co2 2 (co2 4 (co2 8 (co2 16 smaller)))))  ;; =>23


(o2 1 (o2 2 (o2 4 (o2 8 (o2 16 (o2 32 (o2 64 (o2 128 (o2 256 (o2 512 (o2 1024 (o2 2048 larger))))))))))))  ;; => 1327
(co2 1 (co2 2 (co2 4 (co2 8 (co2 16 (co2 32 (co2 64 (co2 128 (co2 256 (co2 512 (co2 1024 (co2 2048 larger))))))))))))  ;; =>3429

(* 1327 3429)  ;; => 4550283




(defn part2 []

  )




