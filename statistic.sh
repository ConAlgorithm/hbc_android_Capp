#!/bin/bash
function count() {
local insert=0
local delete=0
while read line ;do
current=`echo $line| awk -F',' '{printf $2}' | awk '{printf $1}'`
if [[ -n $current ]]; then
insert=`expr $insert + $current`
fi
current=`echo $line | sed -n 's/.*, //p' | awk '{printf $1}'`
if [[ -n $current ]]; then
delete=`expr $delete + $current`
fi
done < .tmp.count
echo "$insert insertions, $delete deletions"
}

function countAll() {
git log --author='jingliang' --shortstat --since="2017-02-01" --before="2017-02-20" --pretty=format:"" | sed /^$/d >.tmp.count

count;
rm .tmp.count
}

function countToday() {
local current=`date +%s`;
local begin=`date +%Y-%m-%d |xargs date +%s -d`;
local minutes=$(($current - $begin));

git log --author='qingcha' --since="$minutes seconds ago" --shortstat --pretty=format:"" | sed /^$/d >.tmp.count
count;
rm .tmp.count

}

function countOneDay() {
git log --author='qingcha' --since="1 days ago" --shortstat --pretty=format:"" | sed /^$/d >.tmp.count
count;
rm .tmp.count

}

if [[ ! -n $1 ]] || [[ $1 = "all" ]] ; then
countAll;
elif [[ $1 = "oneday" ]]; then
countOneDay;
elif [[ $1 = "today" ]]; then
countToday;
else
echo "args: all | oneday | today";
fi
