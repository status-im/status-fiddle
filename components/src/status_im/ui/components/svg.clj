(ns status-im.ui.components.svg
  (:require [clojure.string :as string]
            [hickory.core :as hickory]))

(def svg-tags #{:svg :g :rect :path :use :defs :circle})

(defmacro slurp-svg [file]
  (let [svg (-> (clojure.core/slurp file)
                (string/replace #"[\n]\s*" ""))
        svg-hiccup (hickory/as-hiccup (first (hickory/parse-fragment svg)))
        color (gensym "args")]
    `(fn [~color]
       ~(into []
              (clojure.walk/postwalk-replace
                {:viewbox :viewBox} ;; See https://github.com/jhy/jsoup/issues/272
                (clojure.walk/prewalk
                  (fn [node]
                    (if (svg-tags node)
                      node
                      (if (vector? node)
                        (let [[k v] node]
                          (if (and (= :fill k) v)
                            [k color]
                            node))
                        node)))
                  svg-hiccup))))))