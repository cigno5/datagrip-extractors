# datagrip-extractors

A set of extractors `groovy` files to export query results from JetBrain DataGrip query tool into different formats (because the built-in extractors kind of suck).

## Install

Once located the `extractor` folder of datagrip:

```bash
# set project home
export DEH=$HOME/dev/prj/datagrip-extractors/
# set datagrip extractor folder
export DET="<datagrip extractor folder>"

# symlink extractors to datagrip 
for f in "$DEH"/src/*.groovy; do  ln -s "$f" "$DET/$(basename $f)"; done;
```

