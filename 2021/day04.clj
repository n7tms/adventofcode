(ns aoc2021
  (:require [clojure.string :as string]))

;; TODO: change 00 to current day number
(def day "04")
(def input-file (str "day" day "-input.txt"))

(def small "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7")

(def large (slurp input-file))

;; globally switch between the small and large dataset.
;(def active small)
(def active large)

(def numbers
  (map #(Integer/parseInt %) (string/split (first (string/split-lines active)) #",")))

(def cards
  (for [x (partition 6 (rest (string/split-lines active)))]
    (map #(Integer/parseInt %)
         (rest
          (string/split (string/join " " x) #"\s+")))
    )
)

;; I replace called numbers with a (-1)
(defn mark-cards [cards num]
  (for [mc cards]
    (for [item mc]
      (if (= item num) -1 item)))
)

;; If there is a winning row, it will "add" up to -5
;; Sum all the rows and see if there are any -5's.
(defn winning-row? [card]
  (if (some #(= -5 %)
            (for [x (partition 5 card)]
              (reduce + x)))
    true
    false)
)

;; I first rotate the card so that the columns end up 
;; as rows; then pass the rotated card to winning-row?
;; to check for a winner.
(defn winning-col? [card]
  (let [rotated 
        (for [col (range 0 5)
              row (range 0 5)]
          (nth card (+ (* 5 row) col))
          )]
    (winning-row? rotated)
    )
  )

;; To calculate the winning score, remove all the -1's and
;; add up the remaining numbers. Then multiply by the last
;; number called.
(defn calc-winning-score [card number-called]
  (*
   number-called
   (apply + (remove #(= -1 %) card)))
  )


;; (This was my initial approach. I tweaked it as the coding happened...!)
;; order of play:
;;  . load numbers
;;  . load boards
;;  . LOOP [winner 0
;;  . . if winner, calc winning score
;;  . . Call number
;;  . . loop 
;;  . . . check boards for number
;;  . . . . if found mark with -1 (assoc card idx -1)
;;  . . LOOP
;;  . . . check boards for winner
;;  . . . . winning-row?
;;  . . . . . set winner == to board number
;;  . . . . winning-col? 
;;  . . . . . set winner == to board number


;; find FIRST winning card
;; (The [c n] arguments make it possible to reuse this code in part 2.)
(defn part1 [c n]
  (loop [boards c
         called-number n
         last-called -1]

      ;; Are there any numbers left to call? (prevents an infinite loop)
    (if (empty? called-number)
      "out of numbers"

      ;; Check for a winning board.
      (let [winners (remove #(= -1 %) (for [x (range 0 (count boards))]
                                        (if (or
                                             (winning-row? (nth boards x))
                                             (winning-col? (nth boards x)))
                                          x
                                          -1))) ]
        ;; if there is a winner....
        (if (> (count winners) 0)
          ;; Calculate the winning score
          (calc-winning-score (nth boards (first winners)) last-called)

          ;; otherwise, loop again with the marked boards and the remainder of the numbers
          (recur (mark-cards boards (first called-number)) (rest called-number) (first called-number))))))
)

(part1 cards numbers)  ;; => 35711



;; Find LAST winning card
;; Initially, I ran through until there was only one card remaining, but the score of that card was wrong.
;; The problem was that the last card was not yet a "winner". So I passed that card to part1 with the 
;; remaining numbers and let it play out until it was a winner. Right answer!
(defn part2 []
  (loop [boards cards
         called-number numbers
         last-called -1]


      ;; If we're out of numbers to call, then drop out of the loop.
    (if (empty? called-number)
      "out of numbers"

      ;; Check for winning boards and save the losers.
      (let [losers (remove empty?
                           (for [x (range 0 (count boards))]
                             (if (or
                                  (winning-row? (nth boards x))
                                  (winning-col? (nth boards x)))
                               '()
                               (nth boards x)))) ]
        ;; if only one loser board remains, ....
        (if (= (count losers) 1)
          ;; We still have to play until this last losing board is a winner.
          ;; pass it to part1 to play until it wins.
          (part1 losers (rest called-number))

          ;; otherwise, loop again with the loser boards and the remainder of the numbers
          (recur (mark-cards losers (first called-number)) (rest called-number) (first called-number))))))
  )


(part2)  ;; => 5586

