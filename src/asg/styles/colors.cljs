(ns asg.styles.colors
  (:require [cljs-styled-components.reagent :as styled]
            [asg.styles.typography :as typo]))

(def acadiana-flag {:red    "#c00"
                    :blue   "navy"
                    :yellow "#fd0"})

(def primary (:blue acadiana-flag))
(def secondary (:red acadiana-flag))

(def stripes {:white  "#fff"
              :blue   primary
              :yellow (:yellow acadiana-flag)
              :red    (:red acadiana-flag)})

