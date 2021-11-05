;; Advent of Code
;; Day 20 - 
;;
;; https://www.youtube.com/watch?v=H8bQFKyd7TE&list=PLbPrugU2oQ8VURsQdZ6W_iovXRS24UmZQ&index=20
;;
(ns adventofcode
  (:require [clojure.string :as string]))

"Tile 2311:     Border         Payload
..##.#..#.      ..##.#..#.
##..#.....      #        .     #..#....
#...##..#.      #        .     ...##..#
####.#...#      #        #     ###.#...
##.##.###.      #        .     #.##.###
##...#.###      #        #     #...#.##
.#.#.#..##      .        #     #.#.#..#
..#....#..      .        .     .#....#.
###...#.#.      #        .     ##...#.#
..###..###      ..###..###"



(def small (slurp "day20-input-small.txt"))
(def large (slurp "day20-input.txt"))

(defn border [^String tile, ^long start, ^long end, ^long step]
  (string/join
   (map #(.charAt tile %)
        (range start end step))))

(defn parse-tiles [^String input]
  (for [[ _              id    ^String raw]
        (re-seq #"Tile (\d+):\n([.#\n]{109})" input)]
    {:id (Integer/parseInt id)
     :top    (border raw 0 10 1)
     :bottom (border raw 99 109 1)
     :left   (border raw 0 110 11)
     :right  (border raw 9 119 11)
     :payload (string/join
               (for [y (range 11 99 11)]
                 (.substring raw (+ y 1) (+ y 9))))}))

;; rotate a tile 90 degrees counter-clockwise
(defn rotate [{:keys [id top bottom left right ^String payload]}]
  {:id id
   :top right
   :right (string/reverse bottom)
   :bottom left
   :left (string/reverse top)
   :payload (string/join
             (for [x (range 7 -1 -1)
                   y (range 0 64 8)]
               (.charAt payload (+ y x))))})

(defn flip [{:keys [id top bottom left right ^String payload]}]
  {:id id
   :top bottom
   :bottom top
   :left (string/reverse left)
   :right (string/reverse right)
   :payload (string/join
             (for [y (range 56 -8 -8)]
               (.substring payload y (+ y 8))))})

;; takes a tile "a" and generates possible rotations and flips
(defn symmetries [a]
  (let [b (rotate a)
        c (rotate b)
        d (rotate c)
        e (flip a)
        f (flip b)
        g (flip c)
        h (flip d)]
    [a b c d e f g h]))

;; part 1 ===================================

(defn find-neighbor [origin direction opposite tiles]
  (let [[neighbor]
        (filter (fn [tile]
                  (and
                   (= (direction origin) (opposite tile))
                   (not= (:id origin) (:id tile))))
                tiles)]
    neighbor))

(defn find-edge [origin direction opposite tiles]
  (let [neighbor (find-neighbor origin direction opposite tiles)]
    (if neighbor
      (recur neighbor direction opposite tiles)
      origin)))

(defn part1 []
  (let [tiles (mapcat symmetries (parse-tiles large))
        [somewhere] tiles
        some-top       (find-edge somewhere :top    :bottom tiles)
        top-left       (find-edge some-top  :left   :right  tiles)
        top-right      (find-edge some-top  :right  :left   tiles)
        bottom-left    (find-edge top-left  :bottom :top    tiles)
        bottom-right   (find-edge top-right :bottom :top    tiles)]
    (*
     (:id top-left)
     (:id top-right)
     (:id bottom-left)
     (:id bottom-right))))

(part1)   ;; => 18411576553343

;; part 2 =================================

(defn assemble-grid [tiles]
  (let [[somewhere] tiles
        some-top (find-edge somewhere :top    :bottom  tiles)
        top-left (find-edge some-top  :left   :right   tiles)

        left-edge (take-while some?
                              (iterate #(find-neighbor % :bottom :top tiles) top-left))]

    (for [left left-edge]
      (for [tile (take-while some?
                             (iterate #(find-neighbor % :right :left tiles) left))]
        tile))))

(defn assemble-image [grid]
  (string/join
   (for [line grid
         y (range 0 64 8)
         tile line]
     (-> tile :payload (.substring y (+ y 8))))))

;; Locate the sea monster
;;                   # 
;; #    ##    ##    ###
;;  #  #  #  #  #  #   

(def monster-small
  (-> "
(. . . . . . . . . . . . . . . . . .)#(. .{4}
)#(. . . .)# #(. . . .)# #(. . . .)# # #(.{4})
(.)#(. .)#(. .)#(. .)#(. .)#(. .)#(. . . .{4})
"
      (string/replace #"\s" "")
      re-pattern))


(def monster-large
  (-> "
(. . . . . . . . . . . . . . . . . .)#(. .{76}
)#(. . . .)# #(. . . .)# #(. . . .)# # #(.{76})
(.)#(. .)#(. .)#(. .)#(. .)#(. .)#(. . . .{76})
"
      (string/replace #"\s" "")
      re-pattern))

(def submerge "$1.$2.$3..$4..$5...$6$7.$8.$9.$10.$11.$12.$13")

(defn fix [f x]
  (let [y (f x)]
    (if (= x y)
      x
      (recur f y))))

(defn part2 []
  (apply min
         (for [tiles (->> large
                          parse-tiles
                          (mapcat symmetries)
                          (iterate next)
                          (take 8))]
           (->> tiles
                assemble-grid
                assemble-image
                (fix #(string/replace % monster-large submerge))
                (filter #(= \# %))
                count))))

(part2)  ;; => 2002
