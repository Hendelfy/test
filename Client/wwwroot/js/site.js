let tasksArr;
const tasks = document.querySelector(".tasks")
const menu = document.querySelector(".menu")
const GetData = async () => {
    menu.innerHTML = ''
    tasks.classList.add('hidden')
    await fetch("https://localhost:5001/api/task")
        .then(res => res.json())
        .then(data => {
            tasksArr = data
            const ul = document.createElement("ul")
            menu.appendChild(ul);
            AddingElementsRecursively(data, ul);
        })

}

GetData();

const AddingElementsRecursively = (data, menu, k = 0) => {
    data.forEach(i => {
        if (i.subTasks.length !== 0)
            i.totalDifficulty = totalDifficulty(i)
        const liElement = document.createElement("li")
        const nameInMenu = document.createElement("span")
        const statusIndicator = document.createElement('div')
        liElement.appendChild(nameInMenu)
        liElement.appendChild(statusIndicator)
        i.status = makeStatus(statusIndicator, i.status)
        i.startTime = new Date(i.startTime).toLocaleString()
        nameInMenu.textContent = i.name
        nameInMenu.onclick = () => {
            tasks.classList.remove('hidden')
            const fields = document.querySelectorAll('.tasks > div > p')
            fields.forEach((e) => {
                if (i[e.id] !== undefined) {
                    e.parentNode.classList.remove('hidden')
                    e.textContent = i[e.id]
                } else {
                    e.parentNode.classList.add('hidden')
                }
            })
            // document.querySelector('#edit')
            document.querySelector('#delete').onclick = async ()=>{
                if(!confirm('Are you sure?'))
                    return
                await fetch('https://localhost:5001/api/task/' + i.id,{
                    method: 'delete'
                }).then(async ()=>{
                    await GetData();
                })
            }
        }

        if (i.subTasks.length !== 0) {
            const img = document.createElement("img")
            img.src = "./icons/extender.png"
            liElement.prepend(img)
            const ul = document.createElement("ul")
            ul.classList.add("hidden")
            img.addEventListener("click", () => {
                ul.classList.toggle("hidden")
                img.classList.toggle('opened')

            })
            liElement.appendChild(ul)
            AddingElementsRecursively(i.subTasks, ul, k + 1)
        }

        menu.appendChild(liElement)

    })
}

const makeStatus = (item, statusCode) => {
    item.classList.add('status-indicator')
    let color
    let status
    switch (statusCode) {
        case 0:
            status = 'Assigned'
            color = 'gray'
            break;
        case 1:
            status = 'In Progress'
            color = 'orange'
            break;
        case 2:
            status = 'Paused'
            color = 'red'
            break;
        case 3:
            status = 'Complete'
            color = 'green'
            break;
    }
    item.style.backgroundColor = color
    return status
}

const totalDifficulty = (item) => {
    let sum = item.plannedDifficulty
    if (item.subTasks.length !== 0)
        item.subTasks.forEach(i => {
            sum += totalDifficulty(i)
        })
    return sum
}
