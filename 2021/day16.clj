(ns aoc2021
  (:require [clojure.string :as string]))


(def day "16")
(def input-file (str "day" day "-input.txt"))

(def small "D2FE28")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(defn pad-zero [s n]
  (apply str (take-last n (concat (repeat n \0) s))))

(defn to-binary [d]
  (case d
    "A" "1010"
    "B" "1011"
    "C" "1100"
    "D" "1101"
    "E" "1110"
    "F" "1111"
    (pad-zero (Integer/toString (Integer/parseInt d) 2) 4))
  )

(def input
  (->> small
       (split #"")
       (map #(to-binary %))
       (string/join)))


(defn parse-literal [s]
  "for now, extract the literals and return the rest of the string"
  (loop [in s
         literal ""]
    (if (= "0" (subs in 0 1))
      (subs in 5)   ;; TODO: account for trailing 0's
      (recur (subs in 5) (str literal (subs 1 5)))))
 )

(defn execute-one-packet [cnt input]
  (loop [c     cnt
         i     input
         v-sum     0]
    (if (zero? c) v-sum
        (let [version (subs i 0 3)
              id      (subs i 3 6)]
          (if (= id "100")
            (recur (dec c) (parse-literal (subs i 6)) (+ v-sum version))
            (recur (dec c) (parse-operation (subs i 6)) (+ v-sum version)))))))

(defn parse-operation [s]
  "for now, extract the operations and return the rest of the string"
  (loop [in     s
         len-id (subs in 0 1)
         v-sum  0]
    (if (empty? in)
      v-sum
      (if (= len-id 0)
        (let [sub-length (Integer/parseInt (subs in 1 16))]
          (recur "" 0 (execute-packet (subs 16 sub-length))))

        ;; otherwise, len-id = 1
        ;; -- after a variable number of sub-packets have been processed, how do
        ;; -- I know where we are in the input string?
        (recur )
        )))
  )

(Integer/parseInt "0101" 2)

(defn execute-packet [input]
  (loop [i     input
         v-sum 0]
    (if (empty? i) v-sum
        (let [version (subs i 0 3)
              id      (subs i 3 6)]
          (if (= id "100")
            (recur (parse-literal (subs i 6)) (+ v-sum version))
            (recur (parse-operation (subs i 6)) (+ v-sum version)))))))





;; convert the entire string to binary -- 0s and 1s.
;; read the first three bits
;; . 3 bits = version
;; . 3 bits = id

;; if id = 100 (4), there is a literal number encoded.
;; . read groups of 5 bits.
;; . . if msb is 1, keep the lower 4 bits
;; . . if msb is 0, this is the last 4 bits in the number.

;; if id != 100 (not 4), this is an operational on sub-packets
;; . 



(defn part1 []

  )




(defn part2 []

  )



;; (println "Part1: " (part1) "\nPart2: " (part2))


;; (use 'clojure.test)
;; (deftest sample-data-test
;;   (test/is (= (part1) xx))
;;   (test/is (= (part2) xx)))

;; (run-tests)




