(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.set :as set]))



(def day "08")
(def input-file (str "day" day "-input.txt"))

(def small "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")

(def small2 "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfb cd")

(def small3 
"be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce")

(def large (slurp input-file))

;(def active small3)
(def active large)

(defn split [regex s]
  (string/split s regex))


(def lines
  (->> active
       (string/split-lines)))


(def right-side
  (for [seg7 lines]
    (split #" "
           (let [[_ left right]
                 (re-matches #"(.+) \| (.+)" seg7)]
             right))
    ))

(def freqs
  (frequencies
   (flatten
    (for [ofse right-side]
      (map count ofse)))))


(defn part1 []
  (reduce +
          (for [f freqs]
            (let [[k v] f]
              (if (or (= k 2) (= k 3) (= k 4) (= k 7)) v 0))))
  )  ;; => 539







;; Part2 solution courtesy of https://github.com/tcsullivan/advent-of-code/blob/master/day8/part2.clj

(defn find-match
  "
  Searches for digit that `has segs-left` segments remaining
  after the segments of `dig-to-cmp` are removed from the
  digits in `cnts` with `grp-to-cmp` segments.
  "
  [segs-left dig-to-cmp cnts grp-to-cmp]
  (as-> cnts $
    (filterv #(= (first %) grp-to-cmp) $)
    (filterv
      #(-> (second %)
           (string/replace (re-pattern (string/join ["[" dig-to-cmp "]"])) "")
           (count)
           (= segs-left)
           )
      $
      )
    (get-in $ [0 1])
    )
  )

(defn determine-digits [line]
  (let [counts (mapv #(do [(count %) %]) (take 10 line))
        mcounts (into {} counts)]
    (as-> {} $
        (assoc $ 1 (mcounts 2)
                 4 (mcounts 4)
                 7 (mcounts 3)
                 8 (mcounts 7)
                 )
        (assoc $ 3 (find-match 2 ($ 7) counts 5)
                 6 (find-match 4 ($ 7) counts 6)
                 2 (find-match 3 ($ 4) counts 5)
                 9 (find-match 2 ($ 4) counts 6)
                 )
        (assoc $ 5 (find-match 2 ($ 2) counts 5))
        (assoc $ 0 (find-match 2 ($ 5) counts 6))
        )
    )
  )
 


(defn part2 []
  (reduce
   (fn [sum input]
     (let [line (as-> input $
                  (string/split $ #" ")
                  (mapv (comp string/join sort) $))
           number (subvec line 11 15)
           decoder (set/map-invert (determine-digits line))]
       (->> number
            (map (comp str decoder))
            (string/join)
            (#(Integer/parseInt %))
            (+ sum)
            )))
   0
   (string/split-lines active))
  )   ;; => 1084606



(println "\nPart1: " (part1) "\nPart2: " (part2) "\n")



