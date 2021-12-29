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

;; i just need to look for the first closing delimiter that doesn't
;; match an opening delimiter


(defn part1 []
  (reduce +
          (for [a parsed]
            (loop [line a
                   stack '()]
;(println line "  ----  " stack)
              (if (empty? line)
                0
                (let [fc (str (first line))]
                  (cond
                    ;; Look for an error condition
                    (and (= fc ")") (not (= (first stack) "("))) 3
                    (and (= fc "]") (not (= (first stack) "["))) 57
                    (and (= fc "}") (not (= (first stack) "{"))) 1197
                    (and (= fc ">") (not (= (first stack) "<"))) 25137
                    ;; pop something off the stack?
                    (or (and (= fc ")") (= (first stack) "("))
                        (and (= fc "]") (= (first stack) "["))
                        (and (= fc "}") (= (first stack) "{"))
                        (and (= fc ">") (= (first stack) "<")))
                    (recur (string/join (rest line)) (rest stack))
                    ;; otherwise, push the next open bracket onto the stack
                    :else (recur
                           (string/join (rest line))
                           (cons fc stack))) ))))
))

;; part1 => 311895  in 01:11:55


(defn incomplete-lines []
  (remove number?
          (for [a parsed]
            (loop [line a
                   stack '()]
              
              (if (empty? line)
                stack
                (let [fc (str (first line))]
                  (cond
                    ;; Look for an error condition
                    (and (= fc ")") (not (= (first stack) "("))) 3
                    (and (= fc "]") (not (= (first stack) "["))) 57
                    (and (= fc "}") (not (= (first stack) "{"))) 1197
                    (and (= fc ">") (not (= (first stack) "<"))) 25137
                    ;; pop something off the stack?
                    (or (and (= fc ")") (= (first stack) "("))
                        (and (= fc "]") (= (first stack) "["))
                        (and (= fc "}") (= (first stack) "{"))
                        (and (= fc ">") (= (first stack) "<")))
                    (recur (string/join (rest line)) (rest stack))
                    ;; otherwise, push the next open bracket onto the stack
                    :else (recur
                           (string/join (rest line))
                           (cons fc stack))) )))))
)


(defn total-points []
  (for [lines (incomplete-lines)]
    (loop [c lines
          sum 0]
      (if (empty? c) sum
          (cond
            (= "(" (first c)) (recur (rest c) (+ 1 (* sum 5)))
            (= "[" (first c)) (recur (rest c) (+ 2 (* sum 5)))
            (= "{" (first c)) (recur (rest c) (+ 3 (* sum 5)))
            (= "<" (first c)) (recur (rest c) (+ 4 (* sum 5)))))))
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


;; Research after-the-fact taught me that clojure vectors are stacks.
;; `conj` pushes elements onto the right side of the stack
;; `pop` removes elements from the right side of the stack
;; `peek` looks at the right-side element, without removing it.
(def stack [1 2 3])
(peek stack)    ;; => 3
(pop stack)     ;; => [1 2]
(conj stack 4)  ;; => [1 2 3 4]

;; Use `assoc` to replace elements in a vector
(assoc stack 1 6)  ;; => [1 6 3] (replace the element at idx 1 with the given article. 

(rseq stack)   ;; => (3 2 1)   (note: returns a list, not a vector)

;; `subvec` returns a subset of given vector ... like substring
(subvec stack 1 2)  ;; => [2]



;; Lists can also be used as a stack (who knew?!), but it operates on the front (left)
;; of the list and uses `cons` to push.
(def stackl '(1 2 3))
(peek stackl)   ;; => 1
(pop stackl)    ;; => (2 3)
(cons 0 stackl) ;; => (0 1 2 3)

(def largev (vec (range 100000)))
(def largel      (range 100000))

;; vector operation comparisons
(time (butlast largev))   ;; => 26.9 msec
(time (pop largev))       ;; =>  0.1 msec

(time (last largev))      ;; => 6.5 msec
(time (peek largev))      ;; => 1.2 msec

;; list operation comparisons 
;; (all `lists` are not created equal. pop and peek don't work here.)
(time (rest largel))
(time (pop largel))

(time (first largel))
(time (peek largel))


(deftest stack-stuff
  (testing "vectors"
    (is (= '(1 2) (vec '(1 2))))
    (is (= 3 4)))
  (testing "stuff"
    (is (= 5 4))
    (is (= 1 1))))


(run-tests)

(= '(1 2) (vec '(1 2)))
