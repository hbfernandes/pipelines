#!/usr/bin/env groovy
@Grab(group='org.yaml', module='snakeyaml', version='1.25')

import org.yaml.snakeyaml.Yaml

String input = args ? args[0] : ''
File inFile = new File(input)

if (!input || !inFile.exists()){
  println "Can't find file or no file supplied!"
  return -1
} 

inFile.withReader { reader ->
  Map yaml = new Yaml().load(reader)
  Map groups = yaml.jobGroups as Map

  Map needs = [:]
  Set groupKeys = groups.keySet()
  (1..groupKeys.size()-1).each { i ->
    needs[groupKeys[i]] = (groups[groupKeys[i-1]] as List).collect { it.jobID }
  }

  StringBuilder sb = new StringBuilder()

  sb << "name: ${yaml.buildProperties.BUILD_PLAN_ID}" + '\n'
  sb << 'on: repository_dispatch' + '\n'
  sb << '\n'
  sb << 'jobs:' + '\n'
  
  groups.each { group, List items ->
    items.each { Map item ->
      def matcher = item.scmUrl =~ /https?:\/\/([\w-\.]+)\/(?<org>[\w-]+)\/(?<repo>[\w-]+)\.git/
      matcher.find()

      String directives = 'clean install'
      if(item.directives){
        if(item.directives.startsWith('+=')){
          directives += (item.directives - '+=')
        } else {
          directives = item.directives.replaceAll('deploy', 'install')
        }
      }

                        sb << "  ${item.jobID}:" + '\n'
                        sb << "    runs-on: self-hosted" + '\n'
      if(needs[group])  sb << "    needs: ${needs[group]}" + '\n'
                        sb << "    steps:" + '\n'
                        sb << "      - name: Checkout" + '\n'
                        sb << "        uses: actions/checkout@v1" + '\n'
                        sb << "        with:" + '\n'
                        sb << "          repository: ${matcher.group('org')}/${matcher.group('repo')}" + '\n'
                        sb << "          ref: master" + '\n'
                        sb << "          token: \${{ secrets.SCM_TOKEN }}" + '\n'
                        sb << "          path: pipelines" + '\n'
                        sb << "      - name: Build" + '\n'
                        sb << "        uses: hbfernandes/actions/mvn@master" + '\n'
                        sb << "        with:" + '\n'
      if(item.root)     sb << "          path: ${item.root}" + '\n'
                        sb << "          args: ${directives}" + '\n'
                        sb << '\n'
    }
  }

  // println sb.toString()
  if(args[1]){
    new File(args[1]).write(sb.toString())  
  } else {
    new File('actions_' + inFile.name).write(sb.toString())
  }
}

return 0