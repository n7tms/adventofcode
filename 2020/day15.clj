(ns adventofcode)


(def small "0,3,6")                ;; => 436
(def small2 "0,3,6,0,3,3,1,0,4,0")
(def large "0,13,16,17,1,10,6")



(defn index-of-last [num so-far]
  (inc (.lastIndexOf so-far (int num)))
  )


(def so-far (map #(Integer/parseInt %) (re-seq #"\d+" small2)))

(index-of-last 6 so-far)


(last so-far)

(concat so-far (fn (if (zero? (index-of-last (last so-far) (butlast so-far)))
                      0
                      (- (count so-far) (index-of-last (last so-far) (butlast so-far))))))

;(concat  so-far '(0))
;(index-of-last 0 (butlast so-far))

