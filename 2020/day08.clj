;; Advent of Code 2020
;; Day 08
;;
;; https://adventofcode.com/2020/day/8
;;
;; Description of problem
;; Navigate instruction code
;;
;;
;;
;;

(ns user
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; This is the input data we'll be using.
(def input-file "day08-input.txt")

;; This slurp line is just for testing. (Can I read the input file?)
(slurp input-file)

(def sample 
"nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6")

(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (str/split s regex))

(defn read-lines [filename]
  (str/split-lines (slurp filename)))

;(def aline "acc -99")

(defn read-sample [smpl]
  (str/split-lines smpl))

(def the-code (read-lines input-file))
;(def the-code (read-sample sample))


(defn parse-line [aline]
  (let [[_              op          dist]
;        (re-matches #"(\w{3}) ([\+|\-])(\d+)" aline)
        (re-matches #"(acc|jmp|nop) ([+-]\d+)" aline)
        ]
    {:op op
;     :dir dir
     :dist dist}
    ))  ;; => {:op "acc" :dir "-" :dist "99"}

(parse-line (nth the-code 2))

(defn in? [coll elm]
  (some #(= elm %) coll))


;; This is my recursive/nested implementation. A little slow, but 
;; it's mine and it works.
(defn run [idx visited accum]
  (let [x (parse-line (nth the-code idx))]
    ;(println x idx accum)  ;; used this for testing.
      (cond
        (some? (in? visited idx)) accum
        (= (get x :op) "acc") (run (inc idx) (cons idx visited) (+ accum (Integer/parseInt (get x :dist))))
        (= (get x :op) "jmp") (run (+ idx (Integer/parseInt (get x :dist))) (cons idx visited) accum)
        (= (get x :op) "nop") (run (inc idx) visited accum)
        ))
)


;; Part 1
(run 0 '() 0)  ;; => 1134


;; Part 2






