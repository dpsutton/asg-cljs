(ns asg.sections.banner
  (:require [cljs-styled-components.reagent :refer [defstyled]]
            [asg.styles.colors :as colors]
            [asg.styles.typography :as typo]))

(defstyled BannerText :h1
  {:background         (:blue colors/acadiana-flag)
   :color              "white"
   :font-size          "5em"
   :font-weight        "900"
   :letter-spacing     "0.05em"
   :margin-block-start "0"
   :margin-bottom      "0"
   :text-align         "center"
   :text-shadow        (typo/text-stripper {:colors (map colors/stripes [:yellow :red])
                                            :step   0.035})
   :text-transform     "uppercase"})

(defn Banner []
  [BannerText [:<> "Acadiana" [:br] "Software Group"]])
