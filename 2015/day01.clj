;; Advent of Code 2015
;; Day 01 - Not Quite Lisp
;;
;; 
;; ( ~ up
;; ) ~ down
;;
;; Just count the number of "ups" (+) and "downs" (-) and you should get the final floor.
;;

(def large (slurp "2015-day01-input.txt" ))
(def small "(()))(((") 

(defn part1 []
  (- 
   (count (re-seq #"\(" large))
   (count (re-seq #"\)" large))))

(part1)  ;; => 280


;; After the fact, I found the following "cool" code block at
;; https://github.com/AxlLind/AdventOfCode2015/blob/main/src/01.clj
;; (->> large
;;      frequencies
;;      vals
;;      (apply -)
;;      )
;; uses frequencies idiom to count the parenthesis...
;; pulls out just the values (vals)
;; the apply function does the math, but it returns the wrong floor
;; because frequencies also found a newline in my input, so I ended
;; up with 1 less than the correct answer.
;; Still, a cool implementation.


;; helper function to convert a "(" to a +1 and a ")" to a -1
;; to make it easier to add up.
(defn updown [dir]
  (case dir
    \( 1
    \) -1))

;; Execute part2 by passing it the first index, 0.   => (part2 0)
;; Check if the sum is negative; if it is, return the current index.
;; If it is not negative yet, loop again, incrementing the index, 
;; and adding the current up/down to the sum
(defn part2 [n]
  (loop [idx n sum 0]
    (if (neg? sum) idx
        (recur (inc idx) (+ sum (updown (nth large idx)))))))


(part2 0)   ;; => 1797 


