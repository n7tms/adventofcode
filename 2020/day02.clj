(ns aoc2020-2.core
  (:gen-class))


;; Advent Of Code 2020
;; Problem 2
;;
;; Given a file similar to the following:
;;   1-3 a: abcde
;;   1-3 b: cdefg
;;   2-9 c: ccccccccc
;; verify that the passwords are valid...
;; i.e the first contains 1 to 3 a's, the second
;; contains 1 to 3 b's, and the third contains
;; 2 to 9 c's.
;;
;; Return the number of valid passwords. 


; Slurp the list from a file into a keymap
;    :qty :lttr :pswd
; iterate through list
;   . find the number of :lttr in pswd
;   . is resulting number >= min or <= max?
;   . if true, increment valids
; display the number of valids


(defn set-filename
  "Sets the name of the file to use from the arg received at run time."
  [aFilename]
  (def filename aFilename))

(defn get-list
  "Reads the filename with all the passwords to check."
  [aFilename]
  (slurp aFilename))




(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
