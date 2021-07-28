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

QUOTE = "'"
def all_rows = ROWS.collect { r -> r }

def get_values = { col ->
    return all_rows.collect { row ->
        def str = FORMATTER.format(row, col)
        if (str == 'NULL') {
            str = null
        }
        if (str == null || str.isNumber()) {
            return str
        } else {
            return QUOTE + str.replace(QUOTE, QUOTE + QUOTE) + QUOTE
        }
    }
}

def fieldGen = { col ->
    def field = col.name()
    def values = get_values(col)

    if (values.size() == 1) {
        def v = values[0]
        if (v == null) {
            field += ' IS NULL'
        } else {
            field += ' = ' + v
        }
    } else {
        field += ' IN (' + values.join(', ') + ')'
    }

    return field
}

OUT.append(COLUMNS.collect { col -> fieldGen(col) }.join('\nAND '))
