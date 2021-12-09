;; Advent of Code
;; Day 24 - Lobby Layout
;;

(ns adventofcode
  (:require [clojure.string :as string]))

(def oneline "esewne")

(def small "sesenwnenenewseeswwswswwnenewsewsw
neeenesenwnwwswnenewnwwsewnenwseswesw
seswneswswsenwwnwse
nwnwneseeswswnenewneswwnewseswneseene
swweswneswnenwsewnwneneseenw
eesenwseswswnenwswnwnwsewwnwsene
sewnenenenesenwsewnenwwwse
wenwwweseeeweswwwnwwe
wsweesenenewnwwnwsenewsenwwsesesenwne
neeswseenwwswnwswswnw
nenwswwsewswnenenewsenwsenwnesesenew
enewnwewneswsewnwswenweswnenwsenwsw
sweneswneswneneenwnewenewwneswswnese
swwesenesewenwneswnwwneseswwne
enesenwswwswneneswsenwnewswseenwsese
wnwnesenesenenwwnenwsewesewsesesew
nenewswnwewswnenesenwnesewesw
eneswnwswnwsenenwnwnwwseeswneewsenese
neswnwewnwnwseenwseesewsenwsweewe
wseweeenwnesenwwwswnew")

(def large (slurp "day24-input.txt"))

;(def active oneline)
;(def active small)
(def active large)

(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (string/split s regex))


(defn parse-line [line]
  (flatten
   (loop [ls (split #"" line)
          final '()]
     (if (empty? ls)
       final
       (let [fl (first ls)
             nl (second ls)]
         (cond
           (= fl "e") (recur (rest ls) (conj '("e") final))
           (= fl "w") (recur (rest ls) (conj '("w") final))
           (= fl "n") (recur (rest (rest ls)) (conj (list (str fl nl)) final))
           (= fl "s") (recur (rest (rest ls)) (conj (list (str fl nl)) final)))))))
  )

(def parsed
  (->> active
       string/split-lines
       (map #(parse-line %))
       ))


(def touched-tiles
  (for [tiles parsed]
    (loop [dirs tiles
           tile {:x 0 :y 0}]
      (if (empty? dirs)
        tile
        (let [ft (first dirs)]
          (cond
            (= ft "ne") (recur (rest dirs) (update (update tile :x + 1) :y - 1))
            (= ft "sw") (recur (rest dirs) (update (update tile :x - 1) :y + 1))
            (= ft "e")  (recur (rest dirs) (update (update tile :x + 2) :y + 0))
            (= ft "w")  (recur (rest dirs) (update (update tile :x - 2) :y + 0))
            (= ft "nw") (recur (rest dirs) (update (update tile :x - 1) :y - 1))
            (= ft "se") (recur (rest dirs) (update (update tile :x + 1) :y + 1))
            )))
      )))

(defn part1 []
  (count (filter (fn [[key val]] (odd? val)) (frequencies touched-tiles))))

(part1)  ;; => 339






