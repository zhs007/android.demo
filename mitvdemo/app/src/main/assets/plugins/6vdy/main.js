
  PROJNAME = 'plugins.6vdy';

  console.log(PROJNAME + ' I am OK!');

  {
    let lstdiv = document.getElementsByTagName('div');
    for (let i = 0; i < lstdiv.length; ++i) {
      if (lstdiv[i].style.position == 'fixed') {
        if (lstdiv[i].style.display != 'none') {
          console.log(PROJNAME + ' hide ' + lstdiv[i].id);
          lstdiv[i].style.display = 'none';
        }
      } else if (
        lstdiv[i].id == lstdiv[i].className &&
        lstdiv[i].id.indexOf('l') == 0
      ) {
        if (lstdiv[i].style.display != 'none') {
          console.log(PROJNAME + ' hide ' + lstdiv[i].id);
          lstdiv[i].style.display = 'none';
        }
      } else if (lstdiv[i].id == 'a960') {
        if (lstdiv[i].style.display != 'none') {
          console.log(PROJNAME + ' hide ' + lstdiv[i].id);
          lstdiv[i].style.display = 'none';
        }
      }
    }
  }
