(ns asg.styles.alignment
  (:require [cljs-styled-components.reagent :as styled]))

(styled/defstyled Container
  :div {:margin "0 auto"
        :max-width "960px"
        :padding "1.0875rem 1.45rem"})
