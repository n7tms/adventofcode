(ns aoc2021
  (:require [clojure.string :as string]))


(def day "11")
(def input-file (str "day" day "-input.txt"))

(def small "5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526")

(def vs "6594254334
3857965822")

(def sample "11111
19991
19991
19991
11111")

(def large (slurp input-file))

;(def active sample)
(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))


(def energies
  (vec
   (flatten
    (for [o 
          (->> active
               (string/split-lines)
               (map #(split #"" %)))]
      (map #(Integer/parseInt %) o)))))


(def width 10)
(def height 10)


;; directions like on a clock: 1:00, 3:00, 5:00, 6:00 etc.
(def directions [(inc (- width)) 1 (inc width) width (dec width) -1 (dec (- width)) (- width)])

(defn increase-energy [energy]
  (mapv inc energy))


(defn valid-targets [pos]
  "return a list of flashable targets"
  (vec
   (let [d1 (->> directions
                 (map #(+ pos %))
                 (remove neg?)          ; 12 and some 11 and 1
                 (remove #(< (dec (* width height)) %)) ;;6 and some 5 and 7
                     
                 )]
     (if (zero? (mod pos width))
       ;; remove positions beyond the left edge
       (remove #(= (+ pos (dec width)) %)
               (remove #(= (- pos (inc width)) %) 
                       (remove #(= (dec pos) %) d1)))
       ;; remove positions beyond the right edge
       (if (= 9 (mod pos width))
         (remove #(= (- pos (dec width)) %)
                 (remove #(= (inc pos) %)
                         (remove #(= (+ pos (inc width)) %) d1)))
         d1)))))

(defn return-to-z [engs valid-targets]
  (loop [e engs
         vt valid-targets]
    (if (empty? vt) e
        (recur (assoc e (first vt) 0) (rest vt)))))



;; How to deal with a 10 that gets inc'd to 11, before it gets to flash it's neighbors?
(defn flashable [e]
  (remove nil?
          (for [x (range (count e))]
            (if (>= (nth e x) 10) x))))


(defn flash-pos [pos energies]
  (loop [vt (valid-targets pos)
         e (return-to-z energies vt)]

    (if (empty? vt) e
        (if (= 0 (nth e (first vt)))
          (recur (rest vt) e)
          (recur (rest vt) (assoc e (first vt) (inc (nth e (first vt))))))
        )
))

(defn flash [e flashable]
  (loop [e1 e
         once 0]
    (if (or (zero? once) (> (count (filter #(> % 9) e1)) 0))
      (recur
       (loop [f flashable
              new-e e1]
         (if (empty? f)
           new-e
           (recur (rest f) (flash-pos (first f) new-e))))
       1)
      e1))
)
(def once 0) 
(def e (increase-energy (increase-energy energies))) 
(def f (flashable e)) 
(def rtz (return-to-z e f))
(flash-pos (first f) rtz)
(def vt (valid-targets 2))
(def s1 (return-to-z rtz vt))

(flash rtz f)
(count (filter #(> % 9) rtz))

;(def vt (valid-targets  ))

(defn part1 [goal]
  (loop [steps   0
         flashes 0
         e       energies]
    (if (>= steps goal)
      (do (println e)
          flashes)
      (let [ie (increase-energy e)
            f  (flashable ie)
            oct (flash ie f)]
        (recur (inc steps)
               (+ flashes (count (filter #(= % 0) oct))) ;flashes
               oct)
        )
      )
    )
  )

(part1 1)




(defn part2 []

  )


;(println "Part1: " (part1) "\nPart2: " (part2))



(deftest sample-data-test
  (clojure.test/is (= (part1 100) 1656))
  (clojure.test/is (= (part1 10) 204))
  (clojure.test/is (= (part2) 00)))

(run-tests)

