(ns aoc2021
  (:require [clojure.string :as string]))


(def day "10")
(def input-file (str "day" day "-input.txt"))

(def small "[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(def parsed (string/split-lines active))

;; I just need to look for the first closing delimiter that doesn't
;; match an opening delimiter


(defn part1 []
  (reduce +
          (for [a parsed]
            (loop [line a
                   stack '[]]
;(println line "  ----  " stack)
              (if (empty? line)
                0
                (let [fc (first line)]
                  (cond
                    ;; Look for an error condition
                    (and (= fc \)) (not= (peek stack) \()) 3
                    (and (= fc \]) (not= (peek stack) \[)) 57
                    (and (= fc \}) (not= (peek stack) \{)) 1197
                    (and (= fc \>) (not= (peek stack) \<)) 25137
                    ;; pop something off the stack?
                    (or (and (= fc \)) (= (peek stack) \())
                        (and (= fc \]) (= (peek stack) \[))
                        (and (= fc \}) (= (peek stack) \{))
                        (and (= fc \>) (= (peek stack) \<)))
                    (recur (rest line) (pop stack))
                    ;; otherwise, push the next open bracket onto the stack
                    :else (recur
                           (rest line)
                           (conj stack fc))) ))))
))

(time (part1))
;; part1 => 311895  in 01:11:55


(defn incomplete-lines []
  (remove number?
          (for [a parsed]
            (loop [line a
                   stack []]
              
              (if (empty? line)
                stack
                (let [fc (first line)]
                  (cond
                    ;; Look for an error condition
                    (and (= fc \)) (not= (peek stack) \()) 3
                    (and (= fc \]) (not= (peek stack) \[)) 57
                    (and (= fc \}) (not= (peek stack) \{)) 1197
                    (and (= fc \>) (not= (peek stack) \<)) 25137
                    ;; pop something off the stack?
                    (or (and (= fc \)) (= (peek stack) \())
                        (and (= fc \]) (= (peek stack) \[))
                        (and (= fc \}) (= (peek stack) \{))
                        (and (= fc \>) (= (peek stack) \<)))
                    (recur (rest line) (pop stack))
                    ;; otherwise, push the next open bracket onto the stack
                    :else (recur
                           (rest line)
                           (conj stack fc))) )))))
)

(defn total-points []
  (for [lines (incomplete-lines)]
    (loop [c lines
          sum 0]
      (if (empty? c) sum
          (cond
            (= \( (peek c)) (recur (pop c) (+ 1 (* sum 5)))
            (= \[ (peek c)) (recur (pop c) (+ 2 (* sum 5)))
            (= \{ (peek c)) (recur (pop c) (+ 3 (* sum 5)))
            (= \< (peek c)) (recur (pop c) (+ 4 (* sum 5)))))))
)

(defn part2 []
  (let [srt (sort (total-points))
        idx (quot (count srt) 2)]
    (nth srt idx)
    )
  )

(time (part2))
;; part2  => 2904180541  in 0:27:00


(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (clojure.test/is (= (part1)  26397) "Part 1")
  (clojure.test/is (= (part2) 288957) "Part 2"))

(run-tests)

