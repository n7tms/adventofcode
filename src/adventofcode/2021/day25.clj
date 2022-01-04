(ns adventofcode
  (:require [clojure.string :as string]
))

;; TODO: change 00 to current day number
(def day "25")
(def input-file (str "src/adventofcode/2021/day" day "-input.txt"))

(def small "v...>>.vv>
.vv>>.vv..
>>.>v>...v
>>v>>.>.v.
v>v.vv.v..
>.>>..v...
.vv..>.>v.
v.v..>>v.v
....v..v.>")

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

;; If a cucumber can move, then we can just swap it position:
;; east moving cucumber:   >.  swap the > and .  => .>
;; East moving cucumbers stay in the same row.
;; South moving cucumbers stay in the same column.

;; Transform the input into a vector of vectors
;; But how do I swap positions?

(def input
  (->> active
       string/split-lines
;       (map #(split #"" %))
       (map #(char-array %))
       (map #(chars %))
       (map #(seq %))))


(defn move-east [row]

  (let [fst (first row)
        lst (last row)]
    (loop [row    row
           newrow []]
      (if (empty? row)
        (if (and (= fst \.) (= lst \>))
          (conj (vec(cons lst (butlast (rest newrow)))) \.) 
          newrow)
        (let [cur    (first row)
              nxt    (second row)]

          (if (and (= cur \>) (= nxt \.))
            (recur (rest (rest row)) (conj newrow \. \>))
            (recur (rest row)        (conj newrow cur))))))))

;; (move-east [\> \. \. \. \v \. \. \v \. \>])


(defn move-south [region]
  )


(defn part1 []
  (loop [in input
         moves 0]
    (let [me (move-east in)
          ms (move-south me)]
      (if (= ms in)
        moves
        (recur ms (inc moves))))))






(defn part2 []

  )



;(println "Part1: " (part1) "\nPart2: " (part2))





