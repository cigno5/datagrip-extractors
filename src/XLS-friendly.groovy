/*
 * Available context bindings:
 *   COLUMNS     List<DataColumn>
 *   ROWS        Iterable<DataRow>
 *   OUT         { append() }
 *   FORMATTER   { format(row, col); formatValue(Object, col) }
 *   TRANSPOSED  Boolean
 * plus ALL_COLUMNS, TABLE, DIALECT
 *
 * where:
 *   DataRow     { rowNumber(); first(); last(); data(): List<Object>; value(column): Object }
 *   DataColumn  { columnNumber(), name() }
 */

SEPARATOR = "\t"
QUOTE     = "\""
NEWLINE   = System.getProperty("line.separator")

def printRow = { values, valueToString ->
  values.eachWithIndex { value, idx ->
    def str = valueToString(value)
    if (str.isNumber()) {
        str = str.replace(".", ",")
    }
    if (str == 'NULL') {
        str = ''
    }
    def q = str.contains(SEPARATOR) || str.contains(QUOTE) || str.contains(NEWLINE)
    OUT.append(q ? QUOTE : "")
      .append(str.replace(QUOTE, QUOTE + QUOTE))
      .append(q ? QUOTE : "")
      .append(idx != values.size() - 1 ? SEPARATOR : NEWLINE)
  }
}

if (TRANSPOSED) {
  def values = COLUMNS.collect { new ArrayList<String>() }
  ROWS.each { row -> COLUMNS.eachWithIndex { col, i -> values[i].add(FORMATTER.format(row, col)) } }
  values.each { printRow(it, { it }) }
} else {
  printRow(COLUMNS, { col -> col.name() })
  ROWS.each { row -> printRow(COLUMNS, { FORMATTER.format(row, it) }) }
}