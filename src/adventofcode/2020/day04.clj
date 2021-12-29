;; Advent of Code 2020
;; Day 4
;;
;; https://adventofcode.com/2020/day/4

(ns user
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.walk :as walk]))

;; Sample Data
;ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
;byr:1937 iyr:2017 cid:147 hgt:183cm
;
;iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
;hcl:#cfa07d byr:1929
;
;hcl:#ae17e1 iyr:2013
;eyr:2024
;ecl:brn pid:760753108 byr:1931
;hgt:179cm
;
;hcl:#cfa07d eyr:2025 pid:166559648
;iyr:2011 ecl:brn hgt:59in
;

(def input-file "day04-input.txt")

(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (str/split s regex))

(defn parse-passport [serialized]
  (->> serialized              ; "a:1 b:2 c:3"
       (split #"\s")           ; ["a:1" "b:2" "c:3"]
       (map #(split #":" %))   ; (["a" "1"] ["b" "2"] ["c" "3"])
       (into {})               ; {"a" "1", "b" "2", "c" "3"}
       walk/keywordize-keys    ; {:a "1", :b "2", :c "3"}
        ))

;; This next line is just used to test the parse-passport function.
(parse-passport "a:1 b:2 c:3")


;; Part 1 defines a valid passport by one having all the required fields.
(defn valid-passport-part1? [{:keys [byr iyr eyr hgt hcl ecl pid]}]
  (and byr iyr eyr hgt hcl ecl pid))


;; Part 2 puts some constraints on the values of the fields in the passport.
(defn valid-passport-part2? [{:keys [byr iyr eyr hgt hcl ecl pid]}]
  (and byr iyr eyr hgt hcl ecl pid

  ; part 2 - Field Constraints
  ; byr (Birth Year) - four digits; at least 1920 and at most 2002.
       (<= 1920 (Integer/parseInt byr) 2002)
  ; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
       (<= 2010 (Integer/parseInt iyr) 2020)
  ; eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
       (<= 2020 (Integer/parseInt eyr) 2030)
  ; hgt (Height) - a number followed by either cm or in:
  ;   If cm, the number must be at least 150 and at most 193.
  ;   If in, the number must be at least 59 and at most 76.
       (let [[_             num  unit]
             (re-matches #"(\d+)(cm|in)" hgt)]
         (case unit
           "cm" (<= 150 (Integer/parseInt num) 193)
           "in" (<=  59 (Integer/parseInt num)  76)
           false))
  ; hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
       (re-matches #"#[0-9a-f]{6}" hcl)
  ; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
       (re-matches #"amb|blu|brn|gry|grn|hzl|oth" ecl)
  ; pid (Passport ID) - a nine-digit number, including leading zeroes.
       (re-matches #"\d{9}" pid)
  ; cid (Country ID) - ignored, missing or not.

       ))


(defn part1 []
  (->> input-file
       slurp
       (split #"\n\n")
       (map parse-passport)
       (filter valid-passport-part1?)
       count))

(defn part2 []
  (->> input-file
       slurp
       (split #"\n\n")
       (map parse-passport)
       (filter valid-passport-part2?)
       count))

(part1)    ;; => 213
(part2)    ;; => 147

