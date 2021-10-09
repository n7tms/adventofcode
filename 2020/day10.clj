(ns adventofcode)


(loop [x 1
       y 1]
  (cond
    (> x 10) [x y]
    (even? x) (recur (* x 3) y)
    (odd? y) (recur (inc y) x)
    :otherwise
    [x y])
)

(loop [x 0]
  (cond
    (> x 10) [x]
    :otherwise
    (do
      (println x)
      (recur (inc x)))))

(odd? 0)
