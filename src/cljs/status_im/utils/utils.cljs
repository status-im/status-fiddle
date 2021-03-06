(ns status-im.utils.utils
  (:require [clojure.string :as str]))

(defn show-popup [title content])


(defn show-confirmation
  ([title content on-accept]
   (show-confirmation title content nil on-accept))
  ([title content confirm-button-text on-accept]
   (show-confirmation title content confirm-button-text on-accept nil))
  ([title content confirm-button-text on-accept on-cancel]))

(defn show-question
  ([title content on-accept]
   (show-question title content on-accept nil))
  ([title content on-accept on-cancel]))

(defn http-post
  ([action data on-success]
   (http-post action data on-success nil))
  ([action data on-success on-error]))

(defn http-get
  ([url on-success on-error]
   (http-get url nil on-success on-error))
  ([url valid-response? on-success on-error]))

(defn truncate-str
  "Given string and max threshold, trims the string to threshold length with `...`
  appended to end if length of the string exceeds max threshold, returns the same
  string if threshold is not exceeded"
  [s threshold]
  (if (and s (< threshold (count s)))
    (str (subs s 0 (- threshold 3)) "...")
    s))

(defn clean-text [s]
  (-> s
      (str/replace #"\n" "")
      (str/replace #"\r" "")
      (str/trim)))

(defn first-index
  "Returns first index in coll where predicate on coll element is truthy"
  [pred coll]
  (->> coll
       (keep-indexed (fn [idx e]
                       (when (pred e)
                         idx)))
       first))

(defn hash-tag? [s]
  (= \# (first s)))

(defn wrap-call-once!
  "Returns a version of provided function that will be called only the first time wrapping function is called. Returns nil."
  [f]
  (let [called? (volatile! false)]
    (fn [& args]
      (when-not @called?
        (vreset! called? true)
        (apply f args)
        nil))))

(defn update-if-present
  "Like regular `clojure.core/update` but returns original map if update key is not present"
  [m k f & args]
  (if (contains? m k)
    (apply update m k f args)
    m))

(defn map-values
  "Efficiently apply function to all map values"
  [m f]
  (into {}
        (map (fn [[k v]]
               [k (f v)]))
        m))

(defn deep-merge
  "Recursively merge maps"
  [& maps]
  (if (every? map? maps)
    (apply merge-with deep-merge maps)
    (last maps)))

(defn set-timeout [cb ms])