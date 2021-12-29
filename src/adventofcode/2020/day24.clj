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
;;
;; I mistakenly assumed that with +/- values in the axis that I would always
;; end up on the same hex. Not true! I can take separate paths and the xyz stays
;; unique. e.g. line 2 and line 7 land on the same hex, but the xyz's are unique.



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
            (= ft "ne") (recur (rest t) (update (update tile :x + 1) :y + 1))
            (= ft "sw") (recur (rest t) (update (update tile :x - 1) :y - 1))
            (= ft "e") (recur (rest t) (update tile :x + 1))
            (= ft "w") (recur (rest t) (update tile :x - 1))
            (= ft "se") (recur (rest t) (update (update tile :x + 1) :y - 1))
            (= ft "nw") (recur (rest t) (update (update tile :x - 1) :y - 1))
            ))))))

turned


;; turned contains list of turned tiles
;; filter out the duplicate x,y,z
;; add up the :clr


(defn same-tile? [t1 t2]
  (if (and
       (= (:x t1) (:x t2))
       (= (:y t1) (:y t2))
       (= (:z t1) (:z t2)))
    true
    false))

(loop [t turned
       u '()]
  (let [ft (first t)]
    (do (println ft)
        (if (empty? ft) u
            (let [x (:x ft)
                  y (:y ft)
                  z (:z ft)
                  c (:clr ft)]
              (if (empty? (filter true?
                                  (for [x (rest t)]
                                    (same-tile? ft x))))
                (recur (rest t) (conj u ft))
                (recur (rest t) u))))))
  )





;(for [x parsed]  (println x))







