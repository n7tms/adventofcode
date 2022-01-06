(ns adventofcode
  (:require [clojure.string :as string]
            [clojure.test :as test]))


(def day "05")
(def input-file (str "src/adventofcode/2019/day" day "-input.txt"))

(def small "1002,4,3,4,33,0,0,0,0,0\n")
(def small2 "1101,100,-3,4,0,0,0,0,0\n")
(def large (slurp input-file))

;(def active small2)
(def active large)

(defn split [regex s]
  (string/split s regex))

(def input
  (->> active
       string/trim
       (split #",")
       (mapv #(Integer/parseInt %))))

(defn init-mem [memory noun verb]
  (vec (flatten (list (first memory) noun verb (nthrest memory 3)))))


(defn exc [op ic s1 s2 d]
  (let [ans (op s1 s2)]
    (if (zero? d)
      (vec (flatten (list ans (rest ic))))
      (vec (flatten (list (take d ic) ans (nthrest ic (inc d))))))))

(defn add [memory pc instr]
  (let [p1-mode (Integer/parseInt (subs instr 2 3))
        p2-mode (Integer/parseInt (subs instr 1 2))
        src1 (if (zero? p1-mode) (nth memory (nth memory (+ pc 1))) (nth memory (+ pc 1)))
        src2 (if (zero? p2-mode) (nth memory (nth memory (+ pc 2))) (nth memory (+ pc 2)))
        dest (nth memory (+ pc 3))]
    (exc + memory src1 src2 dest)))

(defn mult [memory pc instr]
  (let [p1-mode (Integer/parseInt (subs instr 2 3))
        p2-mode (Integer/parseInt (subs instr 1 2))
        src1 (if (zero? p1-mode) (nth memory (nth memory (+ pc 1))) (nth memory (+ pc 1)))
        src2 (if (zero? p2-mode) (nth memory (nth memory (+ pc 2))) (nth memory (+ pc 2)))
        dest (nth memory (+ pc 3))]
    (exc * memory src1 src2 dest)))

(defn store [memory pc prgm-input]
  (let [dest (nth memory (+ pc 1))]
;    (println dest prgm-input)
    (vec (flatten (list (take dest memory) prgm-input (nthrest memory (inc dest)))))))

(defn output [memory pc]
  (let [dest (nth memory (+ pc 1))]
    (print (nth memory dest) " ")))

(defn jmp-if-t [memory pc instr]
  (let [p1-mode (Integer/parseInt (subs instr 2 3))
        p2-mode (Integer/parseInt (subs instr 1 2))
        src1 (if (zero? p1-mode) (nth memory (nth memory (+ pc 1))) (nth memory (+ pc 1)))
        src2 (if (zero? p2-mode) (nth memory (nth memory (+ pc 2))) (nth memory (+ pc 2)))
        dest (nth memory (+ pc 3))]
    (if (zero? src1) (+ pc 3) src2)))

(defn jmp-if-f [memory pc instr]
  (let [p1-mode (Integer/parseInt (subs instr 2 3))
        p2-mode (Integer/parseInt (subs instr 1 2))
        src1 (if (zero? p1-mode) (nth memory (nth memory (+ pc 1))) (nth memory (+ pc 1)))
        src2 (if (zero? p2-mode) (nth memory (nth memory (+ pc 2))) (nth memory (+ pc 2)))
        dest (nth memory (+ pc 3))]
    (if (zero? src1) src2 (+ pc 3))))

(defn less-than [memory pc instr]
  (let [p1-mode (Integer/parseInt (subs instr 2 3))
        p2-mode (Integer/parseInt (subs instr 1 2))
        src1 (if (zero? p1-mode) (nth memory (nth memory (+ pc 1))) (nth memory (+ pc 1)))
        src2 (if (zero? p2-mode) (nth memory (nth memory (+ pc 2))) (nth memory (+ pc 2)))
        dest (nth memory (+ pc 3))]
    (if (< src1 src2)
      (vec (flatten (list (take dest memory) 1 (nthrest memory (inc dest)))))
      (vec (flatten (list (take dest memory) 0 (nthrest memory (inc dest))))))))

(defn equals [memory pc instr]
  (let [p1-mode (Integer/parseInt (subs instr 2 3))
        p2-mode (Integer/parseInt (subs instr 1 2))
        src1 (if (zero? p1-mode) (nth memory (nth memory (+ pc 1))) (nth memory (+ pc 1)))
        src2 (if (zero? p2-mode) (nth memory (nth memory (+ pc 2))) (nth memory (+ pc 2)))
        dest (nth memory (+ pc 3))]
    (if (= src1 src2)
      (vec (flatten (list (take dest memory) 1 (nthrest memory (inc dest)))))
      (vec (flatten (list (take dest memory) 0 (nthrest memory (inc dest))))))))


(defn intcode [program prgm-input]
  (loop [in program
         pc 0]

    (let [instr (format "%05d" (nth in pc))
          opcd (Integer/parseInt (subs instr 3))]
;      (println instr " " opcd)
      (cond
        (= opcd 99) (do (println "Execution Ended.") )
        (= opcd  1) (recur (add   in pc instr)      (+ pc 4))
        (= opcd  2) (recur (mult  in pc instr)      (+ pc 4))
        (= opcd  3) (recur (store in pc prgm-input) (+ pc 2))
        (= opcd  4) (do    (output in pc) (recur in (+ pc 2)))
        (= opcd  5) (recur in (jmp-if-t in pc instr))
        (= opcd  6) (recur in (jmp-if-f in pc instr))
        (= opcd  7) (recur (less-than in pc instr)  (+ pc 4))
        (= opcd  8) (recur (equals in pc instr)     (+ pc 4))
        ))))



(defn part1 []
  (intcode input 1)
  )   ;; => 7566643


(defn part2 []
  (intcode input 5)
  )   ;; => 9265694


(println "AoC 2019 - Day 5")
(println "Part1: " (part1) "\nPart2: " (part2))




