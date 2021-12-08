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
(def active small)

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

;oneline
;(parse-line "esewne")

(count parsed)

;; iterate through `parsed`
;; iterate through each set of directions
;; for each set,
;; . create a map {:x 0 :y 0 :z 0 :clr 0}
;; . update the map for each direction in the set
;; . . NE = +x    SW = -x
;; . .  E = +y     W = -y
;; . . SE = +z    NW = -z
;; . . if this is the last direction in the set, invert the color.
;; . . . clr: 0=white; 1=black
;; add up all the :clr values.

(defn invert-color [tile]
;  (println (update tile :clr #(Math/abs (dec %))))
  (update tile :clr #(Math/abs (dec %)))
  )

(defn tile-exists? [tiles tile]
  (filter (fn [[]]))
)



(def turned
  (for [tiles parsed]
    (loop [t tiles
           tile {:x 0 :y 0 :z 0 :clr 0}]

      (if (empty? t)

        (invert-color tile)

        (let [ft (first t)]
          (cond
            (= ft "ne") (recur (rest t) (update tile :x + 1))
            (= ft "sw") (recur (rest t) (update tile :x - 1))
            (= ft "e") (recur (rest t) (update tile :y + 1))
            (= ft "w") (recur (rest t) (update tile :y - 1))
            (= ft "se") (recur (rest t) (update tile :z + 1))
            (= ft "nw") (recur (rest t) (update tile :z - 1))
            ))))))


;; turned contains list of turned tiles
;; filter out the duplicate x,y,z
;; add up the :clr



















